package com.daveace.taskie.api.model

import com.google.gson.annotations.SerializedName

data class TaskErrorResponse(
    val time: String,
    val status: Int,
    @SerializedName("Message")
    val message: String,
    val path: String
)

