package com.poriyaabdollahi.karam.ui.addedittask

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.poriyaabdollahi.karam.R
import com.poriyaabdollahi.karam.databinding.FragmentAddEditTaskBinding
import com.poriyaabdollahi.karam.util.exchaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {
    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditTaskBinding.bind(view)
        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkboxImportant.isChecked = viewModel.taskImportance
            checkboxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text =
                "${viewModel.task?.createdDateFormatted}: ساخته شده در تاریخ "
            when (viewModel.taskColor) {
                "black_light" -> chosenColor.setBackgroundResource(R.color.black_light)
                "pallet_violet" -> chosenColor.setBackgroundResource(R.color.pallet_violet)
                "caroline_blue" -> chosenColor.setBackgroundResource(R.color.caroline_blue)
                "pallet_sea_green" -> chosenColor.setBackgroundResource(R.color.pallet_sea_green)
                "pallet_saffron_yellow" -> chosenColor.setBackgroundResource(R.color.pallet_saffron_yellow)
                "indian_red" -> chosenColor.setBackgroundResource(R.color.indian_red)
                "dark_green" -> chosenColor.setBackgroundResource(R.color.dark_green)
                "violet" -> chosenColor.setBackgroundResource(R.color.violet)
                "green_white" -> chosenColor.setBackgroundResource(R.color.green_white)
                "dark_pink" -> chosenColor.setBackgroundResource(R.color.dark_pink)
            }

            editTextTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }
            checkboxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }
            fabSaveTask.setOnClickListener {

                ObjectAnimator.ofFloat(fabSaveTask, "translationX", -100f).apply {
                    duration = 500

                    if(viewModel.taskName.isNotBlank())start()
                    Timer("SettingUp", false).schedule(500) {
                      viewModel.onSavedClick()
                    }

                }

            }

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.addEditTaskEvent.collect { event ->
                    when (event) {
                        is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
                        is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            binding.editTextTaskName.clearFocus()
                            setFragmentResult(
                                "add_edit_request", bundleOf("add_edit_result" to event.result)
                            )
                            findNavController().popBackStack()
                        }
                    }.exchaustive
                }
            }
            //colors
            blackLight.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.black_light)
                viewModel.taskColor = "black_light"
            }
            palletViolet.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.pallet_violet)
                viewModel.taskColor = "pallet_violet"
            }
            carolineBlue.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.caroline_blue)
                viewModel.taskColor = "caroline_blue"
            }
            seaGreen.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.pallet_sea_green)
                viewModel.taskColor = "pallet_sea_green"
            }
            saffronYellow.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.pallet_saffron_yellow)
                viewModel.taskColor = "pallet_saffron_yellow"

            }
            indianRed.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.indian_red)
                viewModel.taskColor = "indian_red"
            }
            darkGreen.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.dark_green)
                viewModel.taskColor = "dark_green"
            }
            violet.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.violet)
                viewModel.taskColor = "violet"
            }
            greenWhite.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.green_white)
                viewModel.taskColor = "green_white"
            }
            darkPink.setOnClickListener {
                chosenColor.setBackgroundResource(R.color.dark_pink)
                viewModel.taskColor = "dark_pink"
            }
        }
    }
}