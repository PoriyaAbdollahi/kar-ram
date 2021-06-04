package com.poriyaabdollahi.karam.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.poriyaabdollahi.karam.R
import com.poriyaabdollahi.karam.data.SortOrder
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.databinding.FragmentTaskBinding
import com.poriyaabdollahi.karam.databinding.ItemTaskBinding
import com.poriyaabdollahi.karam.util.exchaustive
import com.poriyaabdollahi.karam.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) ,TaskAdapter.onItemClickListener{

    private val viewModel: TaskViewModel by viewModels()
    private  lateinit var searchView : androidx.appcompat.widget.SearchView
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
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return  false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.taskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)
            fabAddTask.setOnClickListener{
                viewModel.onAddNewTaskClicked()
            }
        }
        setFragmentResultListener("add_edit_request"){
            _, bundle-> val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when(event){
                    is TaskViewModel.TaskEvent.showOnDeleteTaskMessage ->{
                        Snackbar.make(requireView(),"Task Deleted",Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TaskViewModel.TaskEvent.NavigateToAddTaskScreen -> {
                   val action = TaskFragmentDirections.actionTaskFragmentToAddEditTaskFragment2(null,"New Task")
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.TaskEvent.NavigateToEditTaskScreen -> {
                        val action = TaskFragmentDirections.actionTaskFragmentToAddEditTaskFragment2(event.task,"Edit Task")
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.TaskEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    TaskViewModel.TaskEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action = TaskFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exchaustive
            }
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
         searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery!=null&&pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery,false)

        }
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
                viewModel.onDeleteAllCompletedClick()
                true
            }
            R.id.action_hide_completed_task->{
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedSelected(item.isChecked)
                true
            }else ->super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }
}