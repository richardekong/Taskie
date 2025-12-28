package com.daveace.taskie.nav

sealed class NavRoutes (val routes:String){

    object New: NavRoutes("New")
    object Tasks : NavRoutes("Tasks")
    object Details: NavRoutes("Details")
    object Edit: NavRoutes("Edit")

}

