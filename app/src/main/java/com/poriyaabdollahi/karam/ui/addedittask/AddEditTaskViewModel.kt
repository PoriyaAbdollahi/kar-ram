package com.poriyaabdollahi.karam.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.data.TaskDAO
import com.poriyaabdollahi.karam.ui.ADD_TASK_RESULT_OK
import com.poriyaabdollahi.karam.ui.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val state : SavedStateHandle,private val taskDao : TaskDAO): ViewModel()  {
    val task = state.get<Task>("task")
    var taskName = state.get<String>("taskName")?: task?.name ?:""
    set(value) {
        field = value
        state.set("taskName",value)
    }
    var taskImportance = state.get<Boolean>("taskImportance")?: task?.important ?:false
        set(value) {
            field = value
            state.set("taskImportance",value)
        }
    private  val  addEditTaskEventChannel =Channel<AddEditTaskEvent>()
    val addEditTaskEvent =addEditTaskEventChannel.receiveAsFlow()
    fun onSavedClick(){
        if (taskName.isBlank()) {
        showInvalidInputMessage("Name Cannot Be Empty")
            return

        }
        if (task != null) {
            val updatedTask =task.copy(name = taskName,important = taskImportance)
            updateTask(updatedTask)
        }else{
            val newTask = Task(name =  taskName , important = taskImportance)
            createTask(newTask)
        }

    }
    private fun createTask(task:Task)  = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))

    }
    private fun  updateTask(task :Task ) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }
    private fun showInvalidInputMessage(text : String)= viewModelScope.launch{
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))

    }
        sealed class AddEditTaskEvent {
            data class ShowInvalidInputMessage(val msg:String) : AddEditTaskEvent()
            data class NavigateBackWithResult(val result:Int) : AddEditTaskEvent()

        }
}