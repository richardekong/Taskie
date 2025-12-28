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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daveace.taskie.model.Task
import tasks
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(modifier: Modifier = Modifier, task: Task = tasks[0]) {

    val cardElevation: CardElevation = CardDefaults.cardElevation(8.dp)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = task.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(5.dp)
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(5.dp)
                ), elevation = cardElevation
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(5.dp),
                text = task.description
            )
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(5.dp)
                ), elevation = cardElevation

        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "Status: ${task.status}")
                Text(text = "Due Date/Time: ${task.dueDateTime.format(DateTimeFormatter.ofPattern("HH:mm, EEEE, dd/MM/yyyy"))}")
            }
        }
    }
}

