package com.daveace.taskie.api.model

data class Task(
    val description: String,
    val dueDateTime: String,
    val id: Long,
    val status: String,
    val title: String
)

