package com.poriyaabdollahi.karam.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.poriyaabdollahi.karam.R
import com.poriyaabdollahi.karam.data.SortOrder
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.databinding.FragmentTaskBinding
import com.poriyaabdollahi.karam.databinding.ItemTaskBinding
import com.poriyaabdollahi.karam.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) ,TaskAdapter.onItemClickListener{

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)
        val taskAdapter = TaskAdapter(this)

        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        setHasOptionsMenu(true)
    }

    override fun onItemClicked(task: Task) {
       viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClicked(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task,isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.onQueryTextChanged {
            // update search view
            viewModel.searchQuery.value = it
        }
    viewLifecycleOwner.lifecycleScope.launch {
        menu.findItem(R.id.action_hide_completed_task).isChecked =
            viewModel.preferenceFlow.first().hideCompleted
    }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     return   when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date ->{
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)

                true
            }
            R.id.action_delete_all_completed_task->{
                true
            }
            R.id.action_hide_completed_task->{
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedSelected(item.isChecked)
                true
            }else ->super.onOptionsItemSelected(item)
        }
    }
}