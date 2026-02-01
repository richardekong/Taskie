package com.daveace.taskie.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class TasksTabViewModel @Inject constructor(): ViewModel(){

    private var _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()
    fun setTabIndex(index:Int){
        _selectedTabIndex.value = index
    }


}