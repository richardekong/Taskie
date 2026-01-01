package com.daveace.taskie.api

data class Task(
    val description: String,
    val dueDateTime: String,
    val id: Int,
    val status: String,
    val title: String
)