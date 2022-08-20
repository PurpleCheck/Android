package com.example.zeroerror.ui.CheckProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zeroerror.R
import com.example.zeroerror.databinding.FragmentScanproductBinding
import com.example.zeroerror.ui.CheckTracking.CheckTrackingActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class ScanProductFragment : Fragment(){

    private lateinit var beepManager: BeepManager
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var viewModel: CheckProductViewModel
    private lateinit var binding: FragmentScanproductBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 1. View Binding 설정
        binding = FragmentScanproductBinding.inflate(inflater, container, false)

        // 2. View Model 설정
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()) .get(
            CheckProductViewModel::class.java)

        // 3. scanner 설정
        beepManager = BeepManager(activity)

        barcodeView = binding.bvCheckProduct
        barcodeView.decoderFactory = DefaultDecoderFactory(arrayListOf(BarcodeFormat.CODE_128, BarcodeFormat.QR_CODE))
        barcodeView.cameraSettings.isAutoFocusEnabled = true
        barcodeView.cameraSettings.requestedCameraId= 0
        barcodeView.initializeFromIntent(activity?.intent)
        barcodeView.decodeContinuous(callback)

        // 4. UI 변경
        binding.tvCurrentProductName.text = getString(R.string.product_list_current_check)
        binding.tvTotalCount.text = "/"+viewModel.productListTotalCount.value.toString()
        binding.tvCheckedCount.text = viewModel.productListCheckedCount.value.toString()
        binding.tvSmallComent.text = "목표 도달까지 ${viewModel.productListTotalCount.value!! - viewModel.productListCheckedCount.value!!}개 더!"
        val progressValue = (viewModel.productListCheckedCount.value!!.toFloat() / viewModel.productListTotalCount.value!!.toFloat() * 100.0f).toInt()
        binding.pbCheckProgress.progress = progressValue

        // progress에 따라 멘트 변경
        when(progressValue){
            in 0..20 -> binding.tvComment.text = getString(R.string.product_list_comment1)
            in 20..40 -> binding.tvComment.text = getString(R.string.product_list_comment2)
            in 40..60 -> binding.tvComment.text = getString(R.string.product_list_comment3)
            in 60..80 -> binding.tvComment.text = getString(R.string.product_list_comment4)
            in 80..100 -> binding.tvComment.text = getString(R.string.product_list_comment5)
        }

        // 5. 모든 order가 검수 된 상태
        if(viewModel.mProductList.filter {it.isChecked}.toList().count()==viewModel.mProductList.count()){
            val intent = Intent(activity, CheckTrackingActivity::class.java)
            intent.putExtra("InvoiceNumber", viewModel.invoiceNumber.value)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    // barcode decode result 처리 callback
    private val callback: BarcodeCallback = object : BarcodeCallback {
        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n")
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null) {
                return
            }
            // 인식한 바코드가 orderList에 있는 경우 - 올바른 상품
            else if(viewModel.productIdList.contains(result.text)){

                // 인식한 바코드의 order 찾기
                val order = viewModel.mProductList.filter { it->it.itemId.toString()==result.text }[0]

                binding.tvCurrentProductName.text = getString(R.string.product_list_current_check) + " " + order.itemName

                if(order.checkCount!=order.totalCount) {
                    // order의 productCheckedCount Update
                    viewModel.updateCount(order)

                    // barcode의 productListCheckedCount Update
                    viewModel.updateListCheckedCount()

                    // UI Update
                    binding.tvCheckedCount.text = viewModel.productListCheckedCount.value.toString()
                    binding.tvSmallComent.text =
                        "목표 도달까지 ${viewModel.productListTotalCount.value!! - viewModel.productListCheckedCount.value!!}개 더!"
                    val progressValue =
                        (viewModel.productListCheckedCount.value!!.toFloat() / viewModel.productListTotalCount.value!!.toFloat() * 100.0f).toInt()
                    binding.pbCheckProgress.progress = progressValue

                    // progress에 따라 멘트 변경
                    when (progressValue) {
                        in 0..20 -> binding.tvComment.text =
                            getString(R.string.product_list_comment1)
                        in 20..40 -> binding.tvComment.text =
                            getString(R.string.product_list_comment2)
                        in 40..60 -> binding.tvComment.text =
                            getString(R.string.product_list_comment3)
                        in 60..80 -> binding.tvComment.text =
                            getString(R.string.product_list_comment4)
                        in 80..100 -> binding.tvComment.text =
                            getString(R.string.product_list_comment5)
                    }

                    // order의 isChecked Update
                    if (!order.isChecked && order.checkCount == order.totalCount) {
                        viewModel.updateIsChecked(order)
                    }

                    // 모든 order가 검수 된 상태 => 화면 이동
                    if (viewModel.mProductList.filter { it.isChecked }.toList()
                            .count() == viewModel.mProductList.count()
                    ) {
                        val intent = Intent(activity, CheckTrackingActivity::class.java)
                        intent.putExtra("InvoiceNumber", viewModel.invoiceNumber.value)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
            // 인식한 바코드가 orderList에 없는 경우 - 잘못된 상품
            else{
                binding.tvCurrentProductName.text = getString(R.string.product_list_current_check)

                // alert dialog - 사용자에게 잘못된 상품임을 경고
                val wrongAlertDialog = WrongProductAlertDialogFragment()
                wrongAlertDialog.show(childFragmentManager,WrongProductAlertDialogFragment.TAG )
            }

            barcodeView.setStatusText(result.text)
            beepManager.playBeepSoundAndVibrate()
            Thread.sleep(1500L)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
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