package com.daveace.taskie.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daveace.taskie.viewmodel.TasksTabViewModel

class TasksTabViewModelFactory(): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksTabViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TasksTabViewModel() as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}