package com.daveace.taskie.api.model

import com.daveace.taskie.api.model.Pageable
import com.google.gson.annotations.SerializedName

data class Tasks(
    @SerializedName("content")
    val tasks: List<Task>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX,
    val totalElements: Int,
    val totalPages: Int
)

