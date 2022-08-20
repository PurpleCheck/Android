package com.example.zeroerror.ui.CheckProduct

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.zeroerror.R
import com.example.zeroerror.ui.CheckInspect.MainActivity

class WrongProductAlertDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.product_list_wrong_product_dialog))
            .setPositiveButton(getString(R.string.product_list_dialog_ok)) { _,_ ->
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton(getString(R.string.product_list_dialog_no)){ _, _ ->
                dismiss()
            }
            .create()

    companion object {
        const val TAG = "WrongProductAlertDialog"
    }
}