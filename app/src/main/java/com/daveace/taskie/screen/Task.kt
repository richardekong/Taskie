package com.daveace.taskie.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.componentUtils.TaskieSnackbarData
import com.daveace.taskie.model.TaskViewModel
import com.daveace.taskie.state.UIState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarController: TaskieSnackbarController,
    taskViewModel: TaskViewModel
) {

    val fetchedTaskState by taskViewModel.fetchedTaskState.collectAsState()

    // Read task by id when taskId is available or not null
    LaunchedEffect(taskViewModel.selectedTaskId) {
        taskViewModel.selectedTaskId?.let { id ->
            taskViewModel.readTaskById(id)
        }
    }

    when (fetchedTaskState) {
        is UIState.Success -> {
            val task:Task? = (fetchedTaskState as UIState.Success<Task?>).data
            if (task != null) {
                TaskDetail(modifier = modifier, task = task)
            }else{
                LoadingScreen()
            }
        }

        is UIState.Error -> {
            ErrorScreen(
                modifier = Modifier,
                errorMessage = (fetchedTaskState as UIState.Error).message
            )
            // Show snackbar to take user from the error page
            val data = TaskieSnackbarData(
                iconResourceId = R.drawable.error_24,
                message = "Please try again!",
                actionLabel = "OK",
                onActionClick = {
                    navController.navigateUp()
                    snackbarController.dismiss()
                }
            )
            snackbarController.showSnackbar(data = data, duration = SnackbarDuration.Long)
        }

        else -> LoadingScreen()
    }

}

@Composable
private fun TaskDetail(modifier: Modifier = Modifier, task: Task?) {

    val cardElevation: CardElevation = CardDefaults.cardElevation(8.dp)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        task?.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(5.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(5.dp)
                ), elevation = cardElevation
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp),
                text = task?.description ?: ""
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(5.dp)
                ), elevation = cardElevation

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "Status: ${task?.status}")
                if (!task?.dueDateTime.isNullOrBlank()){
                    Text(
                        text = "Due Date/Time: ${
                            try {
                                LocalDateTime.parse(task.dueDateTime)
                                    .format(DateTimeFormatter.ofPattern("HH:mm, EEEE, dd/MM/yyyy"))
                            }catch (_: Exception){
                                "Invalidate DateTime"
                            }
                        }"
                    )
                }else{
                    Text(text="Due Date/Time: N/A")
                }
            }
        }
    }
}

