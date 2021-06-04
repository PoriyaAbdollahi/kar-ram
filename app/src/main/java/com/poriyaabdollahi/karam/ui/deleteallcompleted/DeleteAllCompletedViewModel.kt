package com.poriyaabdollahi.karam.ui.deleteallcompleted

import androidx.lifecycle.ViewModel
import com.poriyaabdollahi.karam.data.TaskDAO
import com.poriyaabdollahi.karam.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllCompletedViewModel @Inject constructor(private val taskDao :TaskDAO,@ApplicationScope private val applicationScope :CoroutineScope):ViewModel() {
    fun onConfirmClicked() = applicationScope.launch{
        taskDao.deleteAllCompletedTask()
    }
}