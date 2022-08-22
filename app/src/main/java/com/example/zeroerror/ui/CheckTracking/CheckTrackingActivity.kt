package com.example.zeroerror.ui.CheckTracking


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zeroerror.databinding.ActivityChecktrackingBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckTrackingActivity: AppCompatActivity() {

    private lateinit var beepManager: BeepManager
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var binding: ActivityChecktrackingBinding
    private lateinit var trackingId: String
    private lateinit var inspectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val confirmCheckAlertDialog = ConfirmCheckAlertDialogFragment.newInstance(inspectId)
                confirmCheckAlertDialog.show(supportFragmentManager, ConfirmCheckAlertDialogFragment.TAG)
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