package com.daveace.taskie.screen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.componentUtils.PaginationBar
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.componentUtils.TasksSearchBar
import com.daveace.taskie.model.Status
import com.daveace.taskie.model.Task
import com.daveace.taskie.nav.NavRoutes
import com.daveace.taskie.state.UIState
import com.daveace.taskie.ui.theme.blue
import com.daveace.taskie.ui.theme.light
import com.daveace.taskie.ui.theme.paleOrange
import com.daveace.taskie.viewmodel.TaskCalendarViewModel
import com.daveace.taskie.viewmodel.TaskViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import dev.alejo.compose_calendar.component.DayOfWeekHeader
import dev.alejo.compose_calendar.util.CalendarDefaults
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private data class ClickedCalendarDayItems(val day: CalendarDay, val associatedTasks: List<Task>)

@Composable
fun TaskSchedules(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController
) {
    val tasksState by taskViewModel.fetchedTasksState.collectAsState()
    val calendarViewModel = hiltViewModel<TaskCalendarViewModel>()
    var clickedCalendarDayItems by remember { mutableStateOf<ClickedCalendarDayItems?>(null) }
    var isCalendarDayShown by remember { mutableStateOf(false) }

    when (tasksState) {
        is UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            val tasks: Tasks? = (tasksState as UIState.Success<Tasks?>).data
            Column(
                modifier = modifier
                    .fillMaxSize(0.9F),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isCalendarDayShown) {
                    TaskSchedules(
                        tasks = tasks,
                        onCalendarDayItemClick = { day, tasks ->
                            clickedCalendarDayItems = ClickedCalendarDayItems(day, tasks)
                            // Update calendar startMonth:
                            calendarViewModel.setStartMonth(YearMonth.from(day.date))
                            isCalendarDayShown = true
                        }
                    )

                    PaginationBar(
                        modifier = Modifier,
                        taskViewModel = taskViewModel,
                        tasksState = tasksState,
                        snackbarController = snackbarController,
                    )
                } else {
                    clickedCalendarDayItems?.let { calendarDayItems ->
                        ScheduledTaskScreen(
                            navController = navController,
                            taskViewModel = taskViewModel,
                            clickedCalendarDayItems = calendarDayItems,
                            onDismiss = { isCalendarDayShown = false }
                        )
                    }
                }
            }
        }

        is UIState.Idle, UIState.Empty -> {}
        is UIState.Error -> ErrorScreen()
    }
}

@Composable
private fun TaskSchedules(
    modifier: Modifier = Modifier,
    tasks: Tasks?,
    onCalendarDayItemClick: (CalendarDay, List<Task>) -> Unit = { _, _ -> }
) {

    val calendarViewModel = hiltViewModel<TaskCalendarViewModel>()

    val processedTasks = remember { getProcessedTaskSchedules(tasks) }

    val categorizedTasks = remember { processedTasks?.groupBy { it.dueDateTime?.toLocalDate() } }

    val currentMonth = remember { YearMonth.now() }

    val startMonth by calendarViewModel.startMonth.collectAsState()

    val endMonth by calendarViewModel.endMonth.collectAsState()

    val firstVisibleMonth by calendarViewModel.firstVisibleMonth.collectAsState()

    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberCalendarState(
        startMonth = startMonth ?: YearMonth.from(processedTasks?.let { it[0].dueDateTime }),
        endMonth = endMonth ?: YearMonth.from(processedTasks?.last()?.dueDateTime),
        firstVisibleMonth = firstVisibleMonth ?: currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val yearAndMonth = state.firstVisibleMonth.yearMonth
        .format(DateTimeFormatter.ofPattern("MMMM yyyy"))

    var foundDate by remember { mutableStateOf<LocalDate?>(null) }

    val scope = rememberCoroutineScope()

    val onSearchResultClick: (String) -> Unit = { query ->
        tasks?.tasks?.let {

            val dateString = it
                .filter { task -> task.dueDateTime.isNotBlank() }
                .find { task -> query.equals(task.title, true) }
                ?.dueDateTime

            val extractedDateTime = runCatching { LocalDateTime.parse(dateString) }.getOrNull()

            extractedDateTime?.let { dateTime ->
                // Initialize found date:
                val foundYearMonth = YearMonth.from(dateTime)
                foundDate = dateTime.toLocalDate()
                // Update calendar startMonth:
                calendarViewModel.setStartMonth(foundYearMonth)
                // Move calendar to found year month:
                scope.launch {
                    state.scrollToMonth(foundYearMonth)
                }
            }
        }
    }

    tasks?.let { TasksSearchBar(fetchedTasks = it.tasks, onResultClick = onSearchResultClick) }
    StatusLegend(modifier = Modifier)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = yearAndMonth, style = MaterialTheme.typography.titleMedium)
        HorizontalCalendar(
            state = state,
            monthHeader = {
                DayOfWeekHeader(
                    daysOfWeek = daysOfWeek(firstDayOfWeekFromLocale()),
                    calendarColors = CalendarDefaults.calendarColors()
                )
            },
            dayContent = { day ->
                val tasks = categorizedTasks?.let { it[day.date] }
                CalendarDayContent(
                    day = day,
                    tasksForTheDay = tasks,
                    foundDate = foundDate,
                    onCalendarDayEntryClick = onCalendarDayItemClick
                )
            }
        )
    }
}

@Composable
private fun CalendarDayContent(
    day: CalendarDay,
    tasksForTheDay: List<Task>?,
    foundDate: LocalDate? = null,
    onCalendarDayEntryClick: (CalendarDay, List<Task>) -> Unit = { _, _ -> },
) {
    if (tasksForTheDay == null) {
        CalendarDayContentWithoutTasks(day)
    } else {
        CalendarDayContentWithTasks(day, tasksForTheDay, foundDate, onCalendarDayEntryClick)
    }
}

@Composable
private fun CalendarDayContentWithTasks(
    day: CalendarDay,
    tasksForTheDay: List<Task>,
    foundDate: LocalDate? = null,
    onCalendarDayEntryClick: (CalendarDay, List<Task>) -> Unit = { _, _ -> },
) {

    val isSearchTaskConfirmed = foundDate?.isEqual(day.date) == true
    val maxIndicatorCount = 2
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(
                color = if (isSearchTaskConfirmed) paleOrange else blue,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                onCalendarDayEntryClick.invoke(day, tasksForTheDay)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Text for the day of the date:
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodySmall
            )
            // task indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                tasksForTheDay.take(maxIndicatorCount).forEach { task ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = Status.entries.firstOrNull { entry ->
                                    entry.label.equals(
                                        task.status,
                                        true
                                    )
                                }?.color ?: light,
                                shape = CircleShape
                            )
                    )
                }
                tasksForTheDay.size.let { size ->
                    if (size > maxIndicatorCount) {
                        Text(
                            text = "+${size - maxIndicatorCount}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayContentWithoutTasks(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ScheduledTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    clickedCalendarDayItems: ClickedCalendarDayItems,
    onDismiss: (() -> Unit)? = null
) {
    val dateString = clickedCalendarDayItems.day
        .date.format(DateTimeFormatter.ofPattern(" EEE dd MMM yyyy"))
    Column(modifier = modifier) {
        Text(
            text = "$dateString",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        LazyColumn(modifier = Modifier.wrapContentSize()) {
            val tasks = clickedCalendarDayItems.associatedTasks
            items(items = tasks) {
                TaskDetail(navController = navController, taskViewModel = taskViewModel, task = it)
                if (tasks.size > 1) {
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                modifier = Modifier.align(alignment = Alignment.Bottom),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onDismiss?.invoke()
                }
            ) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TaskDetail(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    task: Task?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Log.d("Task title", "${task?.title}")
        Text(
            text = task?.title ?: "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1F)
        )
        IconButton(
            modifier = Modifier,
            onClick = {
                // update task id
                taskViewModel.setSelectedId(task?.id)
                // Navigate to Task Detail screen:
                navController.navigate(NavRoutes.Details.routes)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.arrow_forward),
                modifier = Modifier
                    .size(16.dp)
                    .weight(0.1F)
            )
        }
    }

}


@Composable
private fun StatusLegend(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Surface(
        modifier = modifier
            .wrapContentSize()
            .animateContentSize()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.keys),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, modifier = Modifier.weight(1F)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "down arrow",
                    modifier = Modifier
                        .size(14.dp)
                        .weight(0.1F)
                        .rotate(rotation)
                        .clickable { expanded = !expanded }
                )

            }
            if (expanded) {
                LazyRow {
                    val entries = Status.entries
                    items(count = entries.size + 2) {
                        if (it < entries.size) {
                            StatusKeyIndicator(
                                entries[it].label,
                                entries[it].color
                            )
                        } else {
                            StatusKeyIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusKeyIndicator(label: String = "Unspecified", color: Color = light) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alignByBaseline()
        )
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color = color, shape = CircleShape)
                .padding(horizontal = 2.dp, vertical = 2.dp)
                .alignByBaseline()
        )
    }
}

private fun getProcessedTaskSchedules(tasks: Tasks?): List<Task>? =
    tasks
        ?.tasks
        ?.filter { it.dueDateTime.isNotBlank() }
        ?.map {
            Task(it)
        }
        ?.sortedBy { it.dueDateTime }

