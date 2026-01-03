package com.daveace.taskie.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Task (
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val dueDateTime: LocalDateTime = LocalDateTime.now()
)

