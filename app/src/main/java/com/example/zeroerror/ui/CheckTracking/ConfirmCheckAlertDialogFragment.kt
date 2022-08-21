package com.example.zeroerror.ui.CheckTracking

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.zeroerror.R
import com.example.zeroerror.data.model.FinishResponse
import com.example.zeroerror.data.network.RetrofitService
import com.example.zeroerror.ui.CheckInspect.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class ConfirmCheckAlertDialogFragment: DialogFragment() {

    private lateinit var inspectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inspectId = arguments?.getString("inspectId") ?: throw IllegalStateException("No args provided")
        Log.d("INSPECT ID", inspectId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.invoice_confirm))
            .setPositiveButton(getString(R.string.invoice_confirm_ok)) { _, _ ->
                patchInspectFinish(inspectId.toLong())
            }
            .create()

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
                                    val intent = Intent(activity?.applicationContext, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    activity?.finish()
                                }
                            }
                        }
                    } else { // response.code == 400 or 300
                        Toast.makeText(activity, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        Log.d("CLIENT_ERR", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<FinishResponse>, t: Throwable) {
                    Toast.makeText(activity, "네트워크 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show()
                    Log.d("RETROFIT_ERR", t.message.toString())
                }
            }
        )
    }


    companion object {
        const val TAG = "ConfirmCheckAlertDialog"

        fun newInstance(
            inspectId: String
        ): ConfirmCheckAlertDialogFragment = ConfirmCheckAlertDialogFragment().apply{
            arguments = Bundle().apply {
                putString("inspectId", inspectId)
            }
        }
    }
}