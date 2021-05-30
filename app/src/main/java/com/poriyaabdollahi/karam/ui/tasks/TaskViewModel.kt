package com.poriyaabdollahi.karam.ui.tasks


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.poriyaabdollahi.karam.data.PreferencesManager
import com.poriyaabdollahi.karam.data.SortOrder
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao :TaskDAO, private  val preferencesManager : PreferencesManager):ViewModel() {
    val searchQuery = MutableStateFlow("")
     val  preferenceFlow = preferencesManager.preferenceFlow
//    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
//    val hideCompleted = MutableStateFlow(true)
   private val  taskFlow  = combine(
        searchQuery,
        preferenceFlow
    )
    {query,filterPreferences ->
        Pair(query,filterPreferences)

    }.flatMapLatest {(query,filterPreferences)->
        taskDao.getAllTasks(query,filterPreferences.sortOrder,filterPreferences.hideCompleted)
    }
    fun onSortOrderSelected(sortOrder:SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }
    fun onHideCompletedSelected(hideCompleted:Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }
    val tasks =taskFlow.asLiveData()


}
