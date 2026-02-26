package com.daveace.taskie.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Task(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val dueDateTime: LocalDateTime? = LocalDateTime.MIN
) {
    constructor(apiTask: com.daveace.taskie.api.model.Task) : this(
        id = apiTask.id,
        title = apiTask.title,
        description = apiTask.description,
        status = apiTask.status,
        dueDateTime = parse(apiTask.dueDateTime)
    )

    private companion object {
        fun parse(value: String) =
            runCatching { LocalDateTime.parse(value) }.getOrNull()
    }

}

