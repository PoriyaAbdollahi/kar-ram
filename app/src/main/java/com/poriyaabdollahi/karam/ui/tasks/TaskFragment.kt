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
import androidx.recyclerview.widget.LinearLayoutManager
import com.poriyaabdollahi.karam.R
import com.poriyaabdollahi.karam.databinding.FragmentTaskBinding
import com.poriyaabdollahi.karam.databinding.ItemTaskBinding
import com.poriyaabdollahi.karam.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)
        val taskAdapter = TaskAdapter()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.onQueryTextChanged {
            // update search view
            viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     return   when (item.itemId) {
            R.id.action_sort_by_name -> {
                true
            }
            R.id.action_sort_by_date ->{
                item.isChecked = !item.isChecked
                true
            }
            R.id.action_delete_all_completed_task->{
                true
            }
            R.id.action_hide_completed_task->{
                true
            }else ->super.onOptionsItemSelected(item)
        }
    }
}