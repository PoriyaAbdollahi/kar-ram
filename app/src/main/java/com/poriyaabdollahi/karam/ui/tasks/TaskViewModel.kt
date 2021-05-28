package com.poriyaabdollahi.karam.ui.tasks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.poriyaabdollahi.karam.data.TaskDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao :TaskDAO):ViewModel() {

    val tasks =taskDao.getAllTask().asLiveData()


}