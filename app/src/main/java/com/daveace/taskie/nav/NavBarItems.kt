package com.daveace.taskie.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info

object NavBarItems {

    val BarItems = listOf(
        BarItem(
            title = "New",
            image = Icons.Filled.AddCircle,
            route = "new"
        ),
        BarItem(
            title = "Details",
            image = Icons.Filled.Info,
            route = "details"
        ),
        BarItem(
            title = "Edit",
            image = Icons.Filled.Edit,
            route = "edit"
        ),
//        BarItem(
//            title = "Tasks",
//            image = Icons.AutoMirrored.Filled.List,
//            route = "tasks"
//        )
    )
}

