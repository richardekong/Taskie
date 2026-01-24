package com.daveace.taskie.model

import androidx.compose.ui.graphics.Color

enum class Status(val label: String, val color: Color) {
    TODO("To do", Color(0xFF0000FF)),
    IN_PROGRESS("In Progress", Color(0xFFFFFF00)),
    COMPLETED("Completed", Color(0xFF00FF00)),
    ON_HOLD("On Hold", Color(0xFFEE82EE)),
    CANCELLED("Cancelled", Color(0xFF000000)),
    PENDING("Pending", Color(0xFFFFA500)),
    REVIEWING("Reviewing", Color(0xFF4B0082)),
    FAILED("Failed", Color(0xFFFF0000)),
    DEFERRED("Deferred", Color(0xFFC0C0C0))
}