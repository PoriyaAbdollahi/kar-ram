package com.poriyaabdollahi.karam.ui.tasks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao :TaskDAO):ViewModel() {
    val searchQuery = MutableStateFlow("")
    private val  taskFlow  = searchQuery.flatMapLatest {
        taskDao.getAllTask(it)
    }
    val tasks =taskFlow.asLiveData()


}