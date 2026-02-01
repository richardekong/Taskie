package com.daveace.taskie.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.daveace.taskie.R
import com.daveace.taskie.componentUtils.TaskieSnackbar
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.componentUtils.TaskieSnackbarData
import com.daveace.taskie.nav.NavBarItems
import com.daveace.taskie.nav.NavRoutes
import com.daveace.taskie.ui.theme.dark
import com.daveace.taskie.ui.theme.light
import com.daveace.taskie.viewmodel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, taskViewModel: TaskViewModel) {

    val navController = rememberNavController()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember(snackbarHostState, scope) {
        TaskieSnackbarController(snackbarHostState, scope)
    }
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    fun backButtonIsShown(): Boolean = currentRoute != NavRoutes.Tasks.routes
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_your_task),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        if (backButtonIsShown()) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                actions = {
                    if (currentRoute == NavRoutes.Details.routes) {
                        ActionMenu(
                            navController = navController,
                            taskViewModel = taskViewModel,
                            snackbarController = snackbarController,
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                NavigationHost(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    snackbarController = snackbarController
                )
            }
        },
        bottomBar = {
//            Not required at this moment
//            BottomNavigationBar(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    snackbarController
                        .taskieSnackbarData?.let { data ->
                            TaskieSnackbar(data = data)
                        }
                })
        },
        floatingActionButton = {
            if (currentRoute != NavRoutes.New.routes) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavRoutes.New.routes)
                    },
                    modifier = modifier.padding(16.dp),
                    containerColor = if (isSystemInDarkTheme()) light else dark,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) dark else light
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController
) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Tasks.routes
    ) {
        composable(NavRoutes.New.routes) {
            CreateTaskScreen(
                taskViewModel = taskViewModel,
                snackbarController = snackbarController,
            )
        }
        composable(NavRoutes.Details.routes) {
            TaskScreen(
                navController = navController,
                snackbarController = snackbarController,
                taskViewModel = taskViewModel
            )
        }
        composable(NavRoutes.Edit.routes) {
            ModifyTaskScreen(
                taskViewModel = taskViewModel,
                snackbarController = snackbarController
            )
        }
        composable(NavRoutes.Tasks.routes) {
            TasksScreen(
                navController = navController,
                taskViewModel = taskViewModel,
                snackbarController = snackbarController
            )
        }

        composable(NavRoutes.Schedules.routes) {
            TaskSchedules(
                navController = navController,
                taskViewModel = taskViewModel,
                snackbarController = snackbarController
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    if (currentRoute != NavRoutes.Tasks.routes && currentRoute != NavRoutes.New.routes) {
        NavigationBar {
            NavBarItems.BarItems.forEach { navItem ->
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = navItem.image,
                            contentDescription = navItem.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(text = navItem.title)
                    }
                )
            }
        }
    }
}

@Composable
fun ActionMenu(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController,
//    scope: CoroutineScope
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.menu)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.update)) },
            onClick = {
                navController.navigate(NavRoutes.Edit.routes)
                expanded = false
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.delete)) },
            onClick = {
                val id = taskViewModel.selectedTaskId
                val data = TaskieSnackbarData(
                    message = "Are you sure?",
                    iconResourceId = R.drawable.question_mark_24,
                    actionLabel = "YES",
                    negativeActionLabel = "NO",
                    onActionClick = {
                        // Delete the current item
                            if (id != null) {
                                taskViewModel.deleteTask(id)
                                navController.navigate(NavRoutes.Tasks.routes)
                            }
                            // Dismiss Snackbar
                            snackbarController.dismiss()
                    },
                    onDismiss = {
                        // Dismiss snackbar on selecting "NO"
                        snackbarController.dismiss()
                    }
                )
                snackbarController.showSnackbar(
                    data = data,
                    duration = SnackbarDuration.Indefinite
                )
                expanded = false
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.edit)
                )
            }
        )
    }
}

