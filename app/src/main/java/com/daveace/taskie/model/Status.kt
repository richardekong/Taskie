package com.daveace.taskie.model

enum class Status(val label: String) {
    TODO("To do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled"),
    PENDING("Pending"),
    REVIEWING("Reviewing"),
    FAILED("Failed"),
    DEFERRED("Deferred")
}