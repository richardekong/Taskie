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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.componentUtils.PaginationBar
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.model.Status
import com.daveace.taskie.state.UIState
import com.daveace.taskie.ui.theme.light
import com.daveace.taskie.viewmodel.TaskViewModel
import dev.alejo.compose_calendar.CalendarEvent
import dev.alejo.compose_calendar.ComposeCalendar
import dev.alejo.compose_calendar.util.CalendarDefaults
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun TaskSchedules(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController
) {

    val tasksState by taskViewModel.fetchedTasksState.collectAsState()
    var clickedCalendarDay by remember { mutableStateOf<ClickedCalendarDay?>(null) }
    var isCalendarDayShown by remember { mutableStateOf(false) }
    when (tasksState) {
        is UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            val tasks: Tasks? = (tasksState as UIState.Success<Tasks?>).data
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Task Schedules",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                if (!isCalendarDayShown) {
                    StatusLegend(modifier = Modifier)
                    TaskSchedules(
                        taskViewModel = taskViewModel,
                        clickedCalendarDay = clickedCalendarDay,
                        tasks = tasks,
                        onCalendarDayClick = { date, schedules ->
                            clickedCalendarDay = ClickedCalendarDay(date, schedules)
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
                    clickedCalendarDay?.let { clickedItem ->
                        ScheduledTaskScreen(
                            clickedCalendarDay = clickedItem,
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
    taskViewModel: TaskViewModel,
    clickedCalendarDay: ClickedCalendarDay?,
    tasks: Tasks?,
    onCalendarDayClick: (LocalDate, List<CalendarEvent<Task>>) -> Unit
) {

    val schedules = getSchedules(tasks)
    val initialDate = getInitialCalendarDate(taskViewModel.selectedTaskId, schedules)
    Log.d("clicked date:", "$initialDate")
    schedules?.let {
        ComposeCalendar(
            modifier = Modifier,
            initDate = clickedCalendarDay?.date ?: initialDate,
            events = it,
            onDayClick = { date, schedules ->
                // Compose a screen of the schedules
                onCalendarDayClick(date, schedules)
            },
            calendarColors = CalendarDefaults.calendarColors(),
            firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
            eventIndicator = { schedule, position, size ->
                if (position < 2) {
                    ScheduledIndicatorContent(schedule = schedule)
                }
                if (position == 2)
                    Text(
                        text = "+${size - 2}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
            },
            maxIndicators = CalendarDefaults.IndicatorLimit.Three,
            indicatorLayout = CalendarDefaults.IndicatorLayout.Grid,
            isContentClickable = true,
            onPreviousMonthClick = {},
            onNextMonthClick = {}
        )
    }

}

private data class ClickedCalendarDay(val date: LocalDate, val schedules: List<CalendarEvent<Task>>)

@Composable
private fun ScheduledTaskScreen(
    modifier: Modifier = Modifier,
    clickedCalendarDay: ClickedCalendarDay,
    onDismiss: (() -> Unit)? = null
) {

    val clickedDate by remember { mutableStateOf(clickedCalendarDay.date) }
    val tasks by remember {
        mutableStateOf(clickedCalendarDay.schedules.map { it.data }.toList())
    }

    Column(modifier = modifier) {
        Text(
            text = "${clickedDate.format(DateTimeFormatter.ofPattern(" EEE dd MMM yyyy"))}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        LazyColumn(modifier = Modifier.wrapContentSize()) {
            items(items = tasks) {
                ExpandableTaskDetail(task = it)
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
private fun ExpandableTaskDetail(task: Task?) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task?.title ?: "",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1F)
        )
        IconButton(
            modifier = Modifier,
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.arrow_down),
                modifier = Modifier
                    .size(16.dp)
                    .weight(0.1F)
                    .rotate(degrees = rotation)
            )
        }
    }
    if (expanded) {
        ExpandedTaskDetailSection(task)
    }
}

@Composable
private fun ExpandedTaskDetailSection(task: Task?) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = task?.description ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Status: ${task?.status ?: ""}",
            style = MaterialTheme.typography.bodyMedium,
        )
        if (!task?.dueDateTime.isNullOrBlank()) {
            val date: String = LocalDateTime.parse(task.dueDateTime)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss EEE dd MMM, yyyy"))
            Text(
                text = "Due date/Time: $date",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ScheduledIndicatorContent(schedule: CalendarEvent<Task>? = null) {
    schedule?.data?.status?.let { status ->
        val color = Status
            .entries
            .firstOrNull { status == it.label }
            ?.color

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(color = color ?: light)
        )
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
                    items(count = entries.size + 1) {
                        if (it < entries.size)
                            StatusKeyIndicator(
                                entries[it].label,
                                entries[it].color
                            )
                        else
                            StatusKeyIndicator()
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

private fun getSchedules(tasks: Tasks?): List<CalendarEvent<Task>>? = tasks
    ?.tasks
    ?.filter { task -> task.dueDateTime.isNotBlank() }
    ?.map { task ->
        CalendarEvent(
            data = task,
            date = LocalDateTime.parse(task.dueDateTime).toLocalDate()
        )
    }
    ?.toList()
    ?.sortedBy { it.date }

private fun getInitialCalendarDate(
    id: Long?,
    calendarEvents: List<CalendarEvent<Task>>?
): LocalDate {
// Extract tasks from the schedules:
    val tasks = calendarEvents?.map { event -> event.data }
        ?.filter { it?.dueDateTime?.isNotBlank() == true }
        ?.toList()
    Log.d("getInitialCalendarDate():", "$tasks")
// Transform tasks to date string:
    val dateString = tasks?.firstOrNull { it?.id == id }?.dueDateTime

// Parse date string to date:
    var date: LocalDate? = null
    try {
        date = LocalDateTime.parse(dateString).toLocalDate()
        Log.d("getInitialCalendarDate():", "$date")
    } catch (e: Exception) {
        Log.d("getInitialCalendarDate():", "${e.message}")
    }
    return date ?: calendarEvents?.get(0)?.date!!
}

