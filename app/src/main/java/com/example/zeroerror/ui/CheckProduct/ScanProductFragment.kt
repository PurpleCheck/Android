package com.example.zeroerror.ui.CheckProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.zeroerror.R
import com.example.zeroerror.databinding.FragmentScanproductBinding
import com.example.zeroerror.ui.CheckInspect.MainActivity
import com.example.zeroerror.ui.CheckTracking.CheckTrackingActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanProductFragment : Fragment(){

    private lateinit var beepManager: BeepManager
    private lateinit var barcodeView: DecoratedBarcodeView

    // View Model 설정
    private val viewModel: CheckProductViewModel by viewModels()
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

        // 2. scanner 설정
        beepManager = BeepManager(activity)

        barcodeView = binding.bvCheckProduct
        barcodeView.decoderFactory = DefaultDecoderFactory(arrayListOf(BarcodeFormat.CODE_128, BarcodeFormat.QR_CODE))
        barcodeView.cameraSettings.isAutoFocusEnabled = true
        barcodeView.cameraSettings.requestedCameraId= 0
        barcodeView.initializeFromIntent(activity?.intent)
        barcodeView.decodeContinuous(callback)

        // 3. UI 변경
        binding.tvCurrentProductName.text = getString(R.string.product_list_current_check)

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
            else if(viewModel.productIdList.value!!.map { it.toString() }.contains(result.text)){

                // 인식한 바코드의 order 찾기
                val order = viewModel.productList.value!!.filter { it.itemId.toString()==result.text }[0]

                binding.tvCurrentProductName.text = getString(R.string.product_list_current_check) + " " + order.itemName

                if(order.checkCount!=order.totalCount) {
                    // order의 productCheckedCount Update
                    viewModel.updateCount(order)

                    // barcode의 productListCheckedCount Update
                    viewModel.updateListCheckedCount()

                    // UI Update
                    binding.tvCheckedCount.text = viewModel.productListCheckedCount.value.toString()
                    binding.tvSmallComent.text = "목표 도달까지 ${viewModel.restCount.value}개 더!"
                    binding.pbCheckProgress.progress = viewModel.progress.value!!

                    // progress에 따라 멘트 변경
                    when (viewModel.progress.value!!) {
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
                    if (viewModel.productList.value!!.filter { it.isChecked }.toList()
                            .count() == viewModel.productList.value!!.count()
                    ) {
                        val intent = Intent(activity?.applicationContext, CheckTrackingActivity::class.java)
                        intent.putExtra("trackingId", viewModel.trackingId.value)
                        intent.putExtra("inspectId", viewModel.inspectItem.value!!.inspectId.toString())
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
                showWrongProductAlertDialog()
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

    // View Model Item Observe
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.productList.observe(viewLifecycleOwner, Observer{

        })

        viewModel.inspectItem.observe(viewLifecycleOwner, Observer {
        })

        viewModel.productListTotalCount.observe(viewLifecycleOwner, Observer {
            binding.tvTotalCount.text = "/"+viewModel.productListTotalCount.value.toString()

        })

        viewModel.productListCheckedCount.observe(viewLifecycleOwner, Observer{
            binding.tvCheckedCount.text = viewModel.productListCheckedCount.value.toString()
        })

        viewModel.restCount.observe(viewLifecycleOwner, Observer{
            binding.tvSmallComent.text = "목표 도달까지 ${viewModel.restCount.value}개 더!"
        })

        viewModel.progress.observe(viewLifecycleOwner, Observer{
            binding.pbCheckProgress.progress = viewModel.progress.value!!
            when(viewModel.progress.value!!){
                in 0..20 -> binding.tvComment.text = getString(R.string.product_list_comment1)
                in 20..40 -> binding.tvComment.text = getString(R.string.product_list_comment2)
                in 40..60 -> binding.tvComment.text = getString(R.string.product_list_comment3)
                in 60..80 -> binding.tvComment.text = getString(R.string.product_list_comment4)
                in 80..100 -> binding.tvComment.text = getString(R.string.product_list_comment5)
            }
        })

        viewModel.productIdList.observe(viewLifecycleOwner, Observer{
        })

        viewModel.trackingId.observe(viewLifecycleOwner, Observer {

        })
        viewModel.inspectId.observe(viewLifecycleOwner, Observer{
        })
    }

    private fun showWrongProductAlertDialog(){
        val alertdialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setMessage(getString(R.string.product_list_wrong_product_dialog))
            .setPositiveButton(getString(R.string.product_list_dialog_ok)) { _,_ ->
                val intent = Intent(activity?.applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton(getString(R.string.product_list_dialog_no)){ DialogInterface, _ ->
                DialogInterface.dismiss()
            }
            .create()

        alertdialog.setCancelable(false)
        alertdialog.show()
    }

}