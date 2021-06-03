package com.poriyaabdollahi.karam.ui.addedittask

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

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {
private val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditTaskBinding.bind(view)
        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkboxImportant.isChecked = viewModel.taskImportance
            checkboxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated. text ="Created : ${viewModel.task?.createdDateFormatted}"

            editTextTaskName.addTextChangedListener{
                viewModel.taskName = it.toString()
            }
            checkboxImportant.setOnCheckedChangeListener{ _,isChecked->
                viewModel.taskImportance = isChecked
            }
            fabSaveTask.setOnClickListener {
                    viewModel.onSavedClick()
            }
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.addEditTaskEvent.collect {event->
                    when(event){
                        is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage->{
                            Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
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

        }
    }
}