package com.example.zeroerror.ui.CheckTracking

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.zeroerror.R
import com.example.zeroerror.ui.CheckInspect.MainActivity

class ConfirmCheckAlertDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.invoice_confirm))
            .setPositiveButton(getString(R.string.invoice_confirm_ok)) { _, _ ->
                // api 호출
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
            .create()

    companion object {
        const val TAG = "ConfirmCheckAlertDialog"
    }
}