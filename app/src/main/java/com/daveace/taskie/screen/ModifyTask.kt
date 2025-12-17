package com.daveace.taskie.screen

import DateTimePickerTextFields
import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.daveace.taskie.model.Status
import com.daveace.taskie.model.Task
import com.daveace.taskie.ui.theme.dark
import tasks
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTaskScreen(modifier: Modifier = Modifier, taskToModify: Task = tasks[0]) {

    var title by remember { mutableStateOf(taskToModify.title) }
    var description by remember { mutableStateOf(taskToModify.description) }
    var status by remember { mutableStateOf(taskToModify.status) }
    var dueDateTime by remember { mutableStateOf(taskToModify.dueDateTime) }
    val dueDateTimeString by remember {
        mutableStateOf(
            dueDateTime.format(
                DateTimeFormatter.ofPattern(
                    "HH:mm:ss, EEEE, dd/MM/yyyy"
                )
            )
        )
    }
    var expanded by remember { mutableStateOf(false) }
    val statusOptions: List<String> = Status.entries.map { it.label }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.modify_this_task),
            style = MaterialTheme.typography.titleMedium
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = dark, shape = RoundedCornerShape(10.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {},
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.title),
                            contentDescription = stringResource(R.string.title_icon),
                            modifier = modifier
                        )
                    },
                    modifier = modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.describe_your_task)) },
                    singleLine = false,
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState())
                )

                ExposedDropdownMenuBox(
                    expanded = expanded, onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = { status = it },
                        label = { Text(stringResource(R.string.status)) },
                        singleLine = true,
                        readOnly = true,
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = modifier
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
                                    status = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                DateTimePickerTextFields(
                    modifier = modifier.fillMaxWidth(), dueDateTime
                ) { dateTime ->
                    dueDateTime = dateTime
                }
            }
        }

        Button(
            modifier = modifier
                .padding(8.dp)
                .align(alignment = Alignment.Start),
            elevation = ButtonDefaults.buttonElevation(4.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val modifiedTask = Task(
                    title = title,
                    description = description,
                    status = status,
                    dueDateTime = dueDateTime
                )
                Log.d("onModifiedTaskClick", modifiedTask.toString())
            }) {
            Text(
                text = stringResource(R.string.modify_task),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }


}