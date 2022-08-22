package com.example.zeroerror.ui.CheckTracking


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.zeroerror.R
import com.example.zeroerror.data.model.FinishResponse
import com.example.zeroerror.data.network.RetrofitService
import com.example.zeroerror.databinding.ActivityChecktrackingBinding
import com.example.zeroerror.ui.CheckInspect.MainActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class CheckTrackingActivity: AppCompatActivity() {

    private lateinit var beepManager: BeepManager
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: ActivityChecktrackingBinding
    private lateinit var trackingId: String
    private lateinit var inspectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // 1. binding 설정
        binding = ActivityChecktrackingBinding.inflate(layoutInflater)

        // 2. scanner 설정
        beepManager = BeepManager(this)

        barcodeView = binding.bvCheckTracking
        barcodeView.decoderFactory = DefaultDecoderFactory(arrayListOf(BarcodeFormat.CODE_128, BarcodeFormat.QR_CODE))
        barcodeView.cameraSettings.isAutoFocusEnabled = true
        barcodeView.cameraSettings.requestedCameraId= 0
        barcodeView.initializeFromIntent(this.intent)
        barcodeView.decodeContinuous(callback)

        trackingId = intent.getStringExtra("trackingId").toString()
        inspectId = intent.getStringExtra("inspectId").toString()

        // 3. ToolBar Setting & Change ToolBar title
        setSupportActionBar(binding.tbCheckTracking)
        supportActionBar?.apply {
            title = "Scan Tracking Id"
            setVisible(true)
        }
        setContentView(binding.root)
    }

    // barcode decode result 처리
    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) {
                return
            }
            // 인식한 바코드가 서버에 저장 된 송장번호일 때 - 올바른 송장
            else if(result.text.equals(trackingId)){
                // 검수 confirm alert dialog 띄우기
                showConfirmCheckAlertDialog()
            }
            // 인식한 바코드가 서버에 없는 송장번호일 때 - 잘못된 송장
            else{
                Toast.makeText(applicationContext, "송장번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            barcodeView.setStatusText(result.text)
            beepManager.playBeepSoundAndVibrate()
            Thread.sleep(2500L)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }


    private fun showConfirmCheckAlertDialog(){
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage(getString(R.string.tracking_confirm))
            .setPositiveButton(getString(R.string.tracking_confirm_ok)) { _, _ ->
                patchInspectFinish(inspectId = inspectId.toLong())
            }
            .create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun patchInspectFinish(inspectId: Long){
        val param = HashMap<String, Boolean>()
        param["completeYN"] = true
        RetrofitService.orderAPI.patchInspectFinish(inspectId=inspectId, param).enqueue(
            object: Callback<FinishResponse> {
                override fun onResponse(call: Call<FinishResponse>, response: Response<FinishResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val body= response.body()
                            body?.let{
                                if(body.finish){
                                    Toast.makeText(applicationContext, "검수 스캔 화면으로 돌아갑니다", Toast.LENGTH_LONG).show()
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    this@CheckTrackingActivity.finish()
                                }
                            }
                        }
                    } else { // response.code == 400 or 300
                        Toast.makeText(applicationContext, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        Log.e("CLIENT_ERR", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "네트워크 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show()
                    Log.e("RETROFIT_ERR", t.message.toString())
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
        Thread.sleep(1500L)
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}