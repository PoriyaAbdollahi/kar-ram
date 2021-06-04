package com.poriyaabdollahi.karam.ui.deleteallcompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment(){
    private val  viewModel : DeleteAllCompletedViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Do you Really  want To delete All completed Tasks ?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){_,_ ->
                viewModel.onConfirmClicked()
            }
            .create()
}