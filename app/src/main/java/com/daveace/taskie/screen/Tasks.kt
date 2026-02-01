package com.daveace.taskie.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.componentUtils.PaginationBar
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.componentUtils.TaskieSnackbarData
import com.daveace.taskie.componentUtils.TasksSearchBar
import com.daveace.taskie.nav.NavRoutes
import com.daveace.taskie.state.UIState
import com.daveace.taskie.ui.theme.crimson
import com.daveace.taskie.ui.theme.green
import com.daveace.taskie.ui.theme.light
import com.daveace.taskie.vector.Calendar
import com.daveace.taskie.viewmodel.TaskViewModel
import com.daveace.taskie.viewmodel.TasksTabViewModel
import dev.alejo.compose_calendar.CalendarEvent
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    snackbarController: TaskieSnackbarController,
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {

    val fetchedTasksState by taskViewModel.fetchedTasksState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var tasks: List<Task> by remember { mutableStateOf(emptyList()) }

    when (fetchedTasksState) {
        is UIState.Loading -> LoadingScreen(modifier)
        is UIState.Success -> {
            val fetchedTasks: List<Task> =
                (fetchedTasksState as UIState.Success<Tasks?>).data?.tasks ?: emptyList()
            Column(modifier = Modifier.fillMaxSize()) {
                Main(
                    modifier = Modifier,
                    navController = navController,
                    taskViewModel = taskViewModel,
                    snackbarController = snackbarController,
                    tasks = fetchedTasks,
                    tasksState = fetchedTasksState
                )
            }
        }

        is UIState.Error -> {
            val message = (fetchedTasksState as UIState.Error).message
            ErrorScreen(modifier = modifier, errorMessage = message)
        }

        else -> {}
    }
}

@Composable
fun Main(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController,
    tasksState: UIState<Tasks?>,
    tasks: List<Task>,
) {

    val tasksTabViewModel = hiltViewModel<TasksTabViewModel>()

    val selectedTabIndex by tasksTabViewModel.selectedTabIndex.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    var searchedCalendarDay by remember { mutableStateOf<ClickedCalendarDay?>(null) }

    var onResultClick by remember{
        mutableStateOf<(String) -> Unit>({})
    }

    fun getMonthIndex(from: YearMonth, to:YearMonth):Int = (to.year - from.year) * 12 + (to.monthValue - from.monthValue)
    data class TabItem(val title: String, val icon: ImageVector)

    val tabItems = listOf(
        TabItem(NavRoutes.Tasks.routes, Icons.AutoMirrored.Filled.List),
        TabItem(NavRoutes.Schedules.routes, Icons.Filled.Calendar)
    )

    Column(modifier = Modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { tasksTabViewModel.setTabIndex(index) },
                    text = { Text(item.title) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(R.string.task_list_tab)
                        )
                    }
                )
            }

        }

        TasksSearchBar(fetchedTasks = tasks, onResultClick = onResultClick)
        when (selectedTabIndex) {
            0 -> {
                onResultClick = { text ->
                    val resultPosition = tasks.indexOf(tasks.find { it.title.equals(text, true) })
                    coroutineScope.launch {
                        listState.scrollToItem(resultPosition)
                    }
                }
                TaskItems(
                    modifier = modifier,
                    navController = navController,
                    taskViewModel = taskViewModel,
                    snackbarController = snackbarController,
                    tasks = tasks,
                    tasksState = tasksState
                )
            }

            1 -> {
                onResultClick = { text ->
                    val scheduledTasks = tasks.filter { it.dueDateTime.isNotBlank() }
                        .map {
                            CalendarEvent(
                                data = it,
                                date = LocalDateTime.parse(it.dueDateTime).toLocalDate()
                            )
                        }
                        .sortedBy { it.date }
                    val searchedSchedule = scheduledTasks.find {
                        it.data?.title.equals(text, true)
                    }

                    val relatedSchedules = scheduledTasks.filter {
                        it.date.isEqual(searchedSchedule?.date)
                    }
                    searchedCalendarDay =
                        ClickedCalendarDay(relatedSchedules[0].date, relatedSchedules)

                }
                Log.d("searched schedules","$searchedCalendarDay")
                TaskSchedules(
                    modifier = Modifier,
                    navController = navController,
                    taskViewModel = taskViewModel,
                    initialCalendarDay = searchedCalendarDay,
                    snackbarController = snackbarController
                )
            }
        }
    }
}

@Composable
fun TaskItems(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarController: TaskieSnackbarController,
    taskViewModel: TaskViewModel,
    tasks: List<Task>,
    tasksState: UIState<Tasks?>
) {
    val listState = rememberLazyListState()
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .semantics { traversalIndex = 1f }
                .fillMaxHeight(0.9F),
            state = listState
        ) {
            items(tasks) {
                Card(
                    modifier = modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    border = CardDefaults.outlinedCardBorder(enabled = true)
                ) {
                    Text(
                        text = it.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                    Row(
                        modifier = modifier
                            .wrapContentSize()
                            .align(Alignment.End)
                            .padding(8.dp)
                    ) {
                        Button(
                            modifier = modifier.wrapContentSize(),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = green, contentColor = light
                            ),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            onClick = {
                                taskViewModel.setSelectedId(it.id)
                                navController.navigate(NavRoutes.Details.routes)
                            }) {
                            Text(
                                text = stringResource(R.string.more),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier.width(2.dp))
                        Button(
                            modifier = modifier.wrapContentSize(),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = crimson, contentColor = light
                            ),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            onClick = {
                                val data = TaskieSnackbarData(
                                    message = "Are you sure?",
                                    iconResourceId = R.drawable.question_mark_24,
                                    actionLabel = "YES",
                                    negativeActionLabel = "NO",
                                    onActionClick = {
                                        // Delete the current item
                                        taskViewModel.deleteTask(it.id)
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
                            }) {
                            Text(
                                text = stringResource(R.string.delete),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        PaginationBar(
            modifier = Modifier,
            tasksState = tasksState,
            snackbarController = snackbarController,
            taskViewModel = taskViewModel
        )
    }
}

