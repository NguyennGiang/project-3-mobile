package com.example.runningtracking.ui.tracking

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.R
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CancelTrackingDialog: DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: () -> Unit){
        yesListener = listener
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialog_AppCompat_Light
        )
            .setTitle("Cancel the run?")
            .setMessage("Are you sure to cancel the run?")
            .setIcon(com.example.runningtracking.R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                yesListener?.let {
                    it()
                }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                run {
                    dialogInterface.cancel()
                }
            }
            .create()
        return dialog
    }
}