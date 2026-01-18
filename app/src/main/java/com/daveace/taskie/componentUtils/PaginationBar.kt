package com.daveace.taskie.componentUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Tasks
import com.daveace.taskie.model.TaskViewModel
import com.daveace.taskie.state.UIState

@Composable
fun PaginationBar(
    modifier: Modifier = Modifier,
    tasksState: UIState<Tasks?>,
    snackbarController: TaskieSnackbarController,
    taskViewModel: TaskViewModel
) {

    val totalPage by remember {
        mutableStateOf(
            if (tasksState is UIState.Success) {
                (tasksState.data?.totalPages ?: 0)
            } else {
                0
            }
        )
    }
    var currentPage by rememberSaveable {
        mutableStateOf(
            if (tasksState is UIState.Success) {
                "${(tasksState.data?.number?.plus(1))}"
            } else {
                ""
            }
        )
    }

    fun makePagedRequest() {
        // create page parameters
        val pageParams = mapOf("page" to "${currentPage.toInt() - 1}")
        // request page of task and update the list of tasks
        taskViewModel.readTasks(pageParams)

    }

    fun showErrorMessage(message: String = "") {

        val data = TaskieSnackbarData(
            message = message,
            iconResourceId = R.drawable.error_24,
            onDismiss = {
                snackbarController.dismiss()
            }
        )

        snackbarController.showSnackbar(
            data,
            duration = SnackbarDuration.Long
        )
    }

    fun goForward() {
        val page = currentPage.toInt()
        if (page < totalPage) currentPage = "${page.inc()}"
    }

    fun goBack() {
        val page = currentPage.toInt()
        if (page in 2..totalPage) currentPage = "${page.dec()}"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .alignByBaseline(),
            text = stringResource(R.string.pages),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
        )
        IconButton(
            modifier = Modifier.weight(0.5F),
            onClick = {
                try {
                    // Update current page and make a paged request of tasks:
                    goBack()
                    // Request a page of tasks:
                    makePagedRequest()
                } catch (_: NumberFormatException) {
                    showErrorMessage("Invalid page number!")
                }
            }) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "arrow back"
            )
        }
        OutlinedTextField(
            value = currentPage,
            onValueChange = {
                currentPage = it
                val page: Int = currentPage.toIntOrNull() ?: 0
                if (page in 1..totalPage) {
                    makePagedRequest()
                } else {
                    showErrorMessage(message = "Invalid page number!")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            label = {},
            textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .wrapContentSize()
                .weight(0.5F)
                .alignByBaseline()
        )
        Text(
            text = "of $totalPage",
            modifier = Modifier
                .weight(0.5F)
                .alignByBaseline(),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
        )
        IconButton(
            modifier = Modifier.weight(0.5F),
            onClick = {
                try {    // Update current page:
                    goForward()
                    // Request a page of tasks:
                    makePagedRequest()
                } catch (_: NumberFormatException) {
                    showErrorMessage("Invalid page number!")
                }
            }) {
            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "Arrow forward",
            )
        }
    }
}
