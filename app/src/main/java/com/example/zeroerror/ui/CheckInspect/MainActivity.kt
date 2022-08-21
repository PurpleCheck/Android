package com.example.zeroerror.ui.CheckInspect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.zeroerror.R
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.network.RetrofitService
import com.example.zeroerror.data.persistence.AppDatabase
import com.example.zeroerror.databinding.ActivityMainBinding
import com.example.zeroerror.ui.CheckProduct.CheckProductActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var beepManager: BeepManager
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. binidng 설정
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 2. scanner 설정
        beepManager = BeepManager(this)

        barcodeView = binding.bvCheckInspect
        barcodeView.decoderFactory = DefaultDecoderFactory(arrayListOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128))
        barcodeView.cameraSettings.isAutoFocusEnabled = true
        barcodeView.cameraSettings.requestedCameraId = 0
        barcodeView.initializeFromIntent(this.intent)
        barcodeView.decodeContinuous(callback)

        // 3. Toolbar 설정
        setSupportActionBar(binding.tbCheckInspect)
        supportActionBar?.apply {
            title = "Scan Inspect Id"
            setVisible(true)
        }
        if(applicationContext.checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
            this.requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        }

        setContentView(binding.root)
    }

    // barcode decode 처리하는 callback 함수
    private val callback: BarcodeCallback = object: BarcodeCallback{

        override fun barcodeResult(result: BarcodeResult) {
            if(result.text == null){
                return
            }
            else{
                getInspectItem(result.text.toLong())
            }
            barcodeView.setStatusText(result.text)
            beepManager.playBeepSoundAndVibrate()
            Thread.sleep(2500L)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getInspectItem(inspectId: Long){
        RetrofitService.orderAPI.getInspectItem(inspectId = inspectId).enqueue(
            object: Callback<Inspect> {
                override fun onResponse(call: Call<Inspect>, response: Response<Inspect>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val body = response.body()
                            body?.let{
                                Log.d("BODY", body.toString())
                                // db에 insert
                                val db = AppDatabase.getInstance(applicationContext)

                                val job = GlobalScope.launch(Dispatchers.IO) {
                                    db?.InspectDao()?.deleteInspectItem()
                                    db?.orderDao()?.deleteAllOrderList()

                                    db?.InspectDao()?.insertInspectItem(body)
                                    db?.orderDao()?.insertOrderList(body.orderList)
                                }

                                runBlocking {
                                    job.join()
                                }
                                // Product Scan 화면으로 전환
                                val intent = Intent(applicationContext, CheckProductActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else { // response.code == 400 or 300
                        Log.e("CLIENT_ERR", response.code().toString())
                        Toast.makeText(applicationContext, "Inspect Id를 다시 스캔해주세요", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Inspect>, t: Throwable) {
                    Log.e("RETROFIT_ERR", t.message.toString())
                    Toast.makeText(applicationContext, "Inspect Id를 다시 스캔해주세요", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}