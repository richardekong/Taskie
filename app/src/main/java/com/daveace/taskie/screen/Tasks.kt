package com.daveace.taskie.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daveace.taskie.R
import com.daveace.taskie.nav.NavRoutes
import com.daveace.taskie.ui.theme.crimson
import com.daveace.taskie.ui.theme.dark
import com.daveace.taskie.ui.theme.green
import com.daveace.taskie.ui.theme.light
import kotlinx.coroutines.launch
import tasks


private val space = 20.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {


    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val onResultClick: (text: String) -> Unit = { text ->
        val resultPosition = tasks.indexOf(tasks.find { it.title.equals(text, true) })
        coroutineScope.launch {
            listState.scrollToItem(resultPosition)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Header(modifier, onResultClick)
        Spacer(modifier.height(space))
        Main(modifier, listState, navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(modifier: Modifier = Modifier, onResultClick: (text: String) -> Unit = {}) {

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchResult by remember { mutableStateOf(emptyList<String>()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = dark, shape = RoundedCornerShape(10.dp))
    ) {
        Text(
            text = stringResource(R.string.tasks),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(alignment = Alignment.CenterHorizontally),
            color = light
        )
        SearchBar(
            modifier = modifier.padding(16.dp),
            expanded = expanded,
            onExpandedChange = { expanded = it },
            shape = RoundedCornerShape(10.dp),
            tonalElevation = 4.dp,
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = { queryText ->
                        text = queryText
                        if (queryText.isEmpty()) {
                            searchResult = emptyList()
                        }
                        searchResult = tasks
                            .map { it.title }
                            .filter {
                                it.contains(queryText, ignoreCase = true) && it.startsWith(
                                    queryText, ignoreCase = true
                                )
                            }
                    },
                    onSearch = {
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(R.string.search_task_by_title)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = modifier.clickable {
                                text = ""
                                expanded = false
                                searchResult = emptyList()
                            })
                    })
            }) {
            Column(modifier = modifier.verticalScroll(state = rememberScrollState())) {
                searchResult.forEach { resultText ->
                    ListItem(
                        headlineContent = { Text(resultText) },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = modifier
                            .clickable {
                                text = resultText
                                expanded = false
                                onResultClick(text)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp))
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(modifier: Modifier = Modifier, listState: LazyListState = rememberLazyListState(), navController: NavController) {
    Box {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.semantics { traversalIndex = 1f },
            state = listState
        ) {
            items(tasks) {
                Card(
                    modifier = modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    border = CardDefaults.outlinedCardBorder(enabled = true)
                ) {
                    Text(
                        text = it.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
                                navController.navigate(NavRoutes.Details.routes)
                            }) {
                            Text(
                                text = stringResource(R.string.more),
                                fontSize = 12.sp,
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
                            onClick = {}) {
                            Text(
                                text = stringResource(R.string.delete),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(NavRoutes.New.routes)
            },
            modifier = modifier
                .align(alignment = Alignment.BottomEnd),
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

