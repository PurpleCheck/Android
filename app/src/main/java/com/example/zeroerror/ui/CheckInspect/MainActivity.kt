package com.example.zeroerror.ui.CheckInspect

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.zeroerror.R
import com.example.zeroerror.databinding.ActivityMainBinding
import com.example.zeroerror.ui.CheckProduct.CheckProductActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

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
                // API response Success
                val intent = Intent(applicationContext, CheckProductActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                // API response Fail
                Toast.makeText(applicationContext, "Inspect Id를 다시 스캔해주세요", Toast.LENGTH_LONG).show()
            }
            barcodeView.setStatusText(result.text)
            beepManager.playBeepSoundAndVibrate()
        }
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