package com.daveace.taskie.state

sealed class UIState<out T> {

    object Loading : UIState<Nothing>()

    data class Success<T>(val data:T): UIState<T>()

    data class Error(val message:String, val exception: Throwable?=null):UIState<Nothing>()

    object Empty : UIState<Nothing>()

    object Idle : UIState<Nothing>()
}

