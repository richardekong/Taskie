package com.daveace.taskie.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TaskCalendarViewModel @Inject constructor(): ViewModel(){
    private val _startMonth = MutableStateFlow<YearMonth?>(null)
    val startMonth = _startMonth.asStateFlow()

    private val _endMonth = MutableStateFlow<YearMonth?>(null)
    val endMonth = _endMonth.asStateFlow()

    private val _firstVisibleMonth = MutableStateFlow(startMonth.value)
    val firstVisibleMonth = _firstVisibleMonth.asStateFlow()

    fun setStartMonth(startMonth: YearMonth){
        this._startMonth.value = startMonth
    }

    fun setEndMonth(endMonth:YearMonth){
        this._endMonth.value = endMonth
    }

    fun setFirstVisibleMonth(firstVisibleMonth:YearMonth){
        this._firstVisibleMonth.value = firstVisibleMonth
    }

}

