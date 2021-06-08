package com.poriyaabdollahi.karam.ui.deleteallcompleted


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.poriyaabdollahi.karam.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment(){
    private val  viewModel : DeleteAllCompletedViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            .setMessage("همه کار های انجام شده پاک شود ؟")
            .setNegativeButton("خیر", null)
            .setPositiveButton("بله"){_,_ ->
                viewModel.onConfirmClicked()
            }
            .create()
}