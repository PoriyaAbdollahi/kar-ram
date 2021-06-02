package com.poriyaabdollahi.karam.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.poriyaabdollahi.karam.data.Task
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(private val state : SavedStateHandle,taskDao : TaskDAO): ViewModel()  {
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


}