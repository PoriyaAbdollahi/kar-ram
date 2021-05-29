package com.poriyaabdollahi.karam.ui.tasks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao :TaskDAO):ViewModel() {
    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(true)
    private val  taskFlow  = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    )
    {query,sortOrder,hideCompleted ->
        Triple(query,sortOrder,hideCompleted)


    }.flatMapLatest {(query,sortOrder,hideCompleted)->
        taskDao.getAllTasks(query,sortOrder,hideCompleted)
    }
    val tasks =taskFlow.asLiveData()


}
enum class SortOrder {BY_NAME,BY_DATE}