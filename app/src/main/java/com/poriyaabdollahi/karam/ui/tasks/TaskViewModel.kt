package com.poriyaabdollahi.karam.ui.tasks


import android.content.Context
import androidx.lifecycle.*
import com.poriyaabdollahi.karam.data.PreferencesManager
import com.poriyaabdollahi.karam.data.SortOrder
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao :TaskDAO, private  val preferencesManager : PreferencesManager,private val state : SavedStateHandle):ViewModel() {
    val searchQuery = state.getLiveData("searchQuery","")
     val  preferenceFlow = preferencesManager.preferenceFlow
  private  val  tasksEventChannel = Channel<TaskEvent>()
    val taskEvent = tasksEventChannel.receiveAsFlow()

   private val  taskFlow  = combine(
        searchQuery.asFlow(),
        preferenceFlow
    )
    {query,filterPreferences ->
        Pair(query,filterPreferences)

    }.flatMapLatest {(query,filterPreferences)->
        taskDao.getAllTasks(query,filterPreferences.sortOrder,filterPreferences.hideCompleted)
    }
    val tasks =taskFlow.asLiveData()

    fun onSortOrderSelected(sortOrder:SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }
    fun onHideCompletedSelected(hideCompleted:Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch{
        tasksEventChannel.send(TaskEvent.NavigateToEditTaskScreen(task))
    }
    fun onTaskCheckedChanged(task: Task,isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun taskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasksEventChannel.send(TaskEvent.showOnDeleteTaskMessage(task))
    }
    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }
    fun onAddNewTaskClicked() = viewModelScope.launch{
    tasksEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }
    sealed class TaskEvent{
        object NavigateToAddTaskScreen : TaskEvent()
        data class NavigateToEditTaskScreen(val task : Task) : TaskEvent()
        data class showOnDeleteTaskMessage(val task: Task):TaskEvent()

    }
}
