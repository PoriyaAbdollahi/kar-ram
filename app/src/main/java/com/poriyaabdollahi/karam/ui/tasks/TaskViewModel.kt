package com.poriyaabdollahi.karam.ui.tasks


import androidx.lifecycle.*
import com.poriyaabdollahi.karam.data.*
import com.poriyaabdollahi.karam.ui.ADD_TASK_RESULT_OK
import com.poriyaabdollahi.karam.ui.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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

    }.flatMapLatest { (query,filterPreferences)->
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
        tasksEventChannel.send(TaskEvent.ShowOnDeleteTaskMessage(task))
    }
    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }
    fun onAddNewTaskClicked() = viewModelScope.launch{
    tasksEventChannel.send(TaskEvent.NavigateToAddTaskScreen)
    }
    fun onShowCaseDone(){
        preferencesManager.writeUpdateShowCase()
    }
    fun readShowCase(): Boolean{
      return  preferencesManager.readShowCase()
    }
    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("کار اضافه شد")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("کار بروزرسانی شد")
        }

    }

    private fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        tasksEventChannel.send(TaskEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick()= viewModelScope.launch {
        tasksEventChannel.send(TaskEvent.NavigateToDeleteAllCompletedScreen)
    }


    sealed class TaskEvent{
        object NavigateToAddTaskScreen : TaskEvent()
        data class NavigateToEditTaskScreen(val task : Task) : TaskEvent()
        data class ShowOnDeleteTaskMessage(val task: Task):TaskEvent()
        data class ShowTaskSavedConfirmationMessage(val msg:String) :TaskEvent()
        object NavigateToDeleteAllCompletedScreen : TaskEvent()
    }
}
