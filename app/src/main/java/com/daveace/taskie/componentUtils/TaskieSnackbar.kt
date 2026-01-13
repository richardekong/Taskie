package com.daveace.taskie.componentUtils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daveace.taskie.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class TaskieSnackbarData(
    val message: String = "",
    val iconResourceId: Int?,
    val actionLabel: String = "",
    val negativeActionLabel:String? = null,
    val onActionClick: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null
)

class TaskieSnackbarController(
    private val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    var taskieSnackbarData by mutableStateOf<TaskieSnackbarData?>(null)
        private set

    private var job: Job? = null

    fun showSnackbar(
        data: TaskieSnackbarData,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        // Cancel an existing job
        job?.cancel()
        // Launch a new job
        job = scope.launch {
            taskieSnackbarData = data
            hostState.showSnackbar(message = "", duration = duration)
        }
        taskieSnackbarData = null
    }

    fun dismiss(){
        // Cancel the job
        job?.cancel()
        hostState.currentSnackbarData?.dismiss()
        taskieSnackbarData = null
    }
}

@Composable
fun TaskieSnackbar(
    data: TaskieSnackbarData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (data.iconResourceId != null) {
                    Icon(
                        painter = painterResource(data.iconResourceId),
                        contentDescription = stringResource(R.string.taskie_snackbar_icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(R.string.taskie_snackbar_icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = data.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                data.onActionClick?.let{ action ->
                    TextButton(onClick = { action.invoke()}) {
                        Text(text = data.actionLabel, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                data.onDismiss?.let { dismiss ->
                    TextButton(
                        onClick = { dismiss.invoke() }
                    ) {
                        Text(
                            text = data.negativeActionLabel?:stringResource(R.string.close),
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TaskieSnackbarContent(
    icon: ImageVector = Icons.Filled.Info,
    data: SnackbarData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.taskie_snackbar_icon),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = data.visuals.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Button(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        data.dismiss()
                    }) {
                    Text(
                        text = data.visuals.actionLabel ?: stringResource(R.string.ok),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

