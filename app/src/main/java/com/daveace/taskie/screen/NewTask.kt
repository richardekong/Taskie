package com.daveace.taskie.screen

import DateTimePickerTextFields
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Task
import com.daveace.taskie.componentUtils.TaskieSnackbarController
import com.daveace.taskie.componentUtils.TaskieSnackbarData
import com.daveace.taskie.model.Status
import com.daveace.taskie.viewmodel.TaskViewModel
import com.daveace.taskie.state.UIState
import com.daveace.taskie.ui.theme.dark


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskScreen(
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel,
    snackbarController: TaskieSnackbarController
) {

    val createdTaskState by taskViewModel.createdTaskState.collectAsState()

    when (createdTaskState) {

        is UIState.Idle -> TaskForm(modifier = modifier, taskViewModel = taskViewModel)
        is UIState.Success -> {
            TaskForm(modifier = modifier, taskViewModel = taskViewModel)
            // notify user by snackbar:
            snackbarController.showSnackbar(
                data = TaskieSnackbarData(
                    iconResourceId = R.drawable.info_24,
                    message = (createdTaskState as UIState.Success<String>).data,
                    actionLabel = stringResource(R.string.ok),
                    onDismiss = {
                        taskViewModel.resetCreatedTaskState()
                        snackbarController.dismiss()
                    }),
                duration = SnackbarDuration.Indefinite
            )
        }

        is UIState.Error -> {
            // notify user by error page:
            ErrorScreen(
                modifier = Modifier,
                errorMessage = (createdTaskState as UIState.Error).message,
                onDismiss = {
                    taskViewModel.resetCreatedTaskState()
                }
            )
        }

        else -> {
            // other if state is loading, empty or idle:
            LoadingScreen()
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TaskForm(modifier: Modifier, taskViewModel: TaskViewModel) {

    var expanded by remember { mutableStateOf(false) }
    val statusOptions: List<String> = Status.entries.map { it.label }
    val radius = 10.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.schedule_a_new_task),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(4.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = dark, shape = RoundedCornerShape(radius)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                OutlinedTextField(
                    value = taskViewModel.title,
                    onValueChange = taskViewModel::onTitleChange,
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    shape = RoundedCornerShape(radius),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.title),
                            contentDescription = stringResource(R.string.title_icon),
                            modifier = Modifier
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = taskViewModel.description,
                    onValueChange = taskViewModel::onDescriptionChange,
                    label = { Text(stringResource(R.string.describe_your_task)) },
                    singleLine = false,
                    shape = RoundedCornerShape(radius),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState())
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    OutlinedTextField(
                        value = taskViewModel.status,
                        onValueChange = taskViewModel::onStatusChange,
                        label = { Text(stringResource(R.string.status)) },
                        singleLine = true,
                        readOnly = true,
                        shape = RoundedCornerShape(radius),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor(
                                type = MenuAnchorType.PrimaryEditable,
                                enabled = true
                            )
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statusOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    taskViewModel.onStatusChange(it)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                DateTimePickerTextFields(
                    modifier = Modifier.fillMaxWidth(),
                    initialDateTime = taskViewModel.dueDateTime,
                    onDateTimeSelected = taskViewModel::onDueDateTimeChange
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.Start),
            elevation = ButtonDefaults.buttonElevation(4.dp),
            shape = RoundedCornerShape(radius),
            onClick = {
                // Create task object from user inputs:
                val newTask = Task(
                    title = taskViewModel.title,
                    description = taskViewModel.description,
                    status = taskViewModel.status,
                    dueDateTime = taskViewModel.dueDateTime.toString()
                )
                // Pass the task to the repository via the view model
                taskViewModel.createTask(newTask)
            }) {
            Text(
                text = stringResource(R.string.create_task),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

