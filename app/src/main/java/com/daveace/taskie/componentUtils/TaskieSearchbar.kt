package com.daveace.taskie.componentUtils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daveace.taskie.R
import com.daveace.taskie.api.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksSearchBar(
    modifier: Modifier = Modifier,
    fetchedTasks: List<Task> = listOf(),
    onResultClick: (text: String) -> Unit = {}
) {

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchResult by remember { mutableStateOf(emptyList<String>()) }

    Box(modifier = modifier
        .fillMaxWidth()
        .padding(top = 0.dp)) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                        searchResult = fetchedTasks.map { it.title }
                            .filter {
                                it.contains(queryText, ignoreCase = true) && it.startsWith(
                                    queryText, ignoreCase = true
                                )
                            }
                    }, onSearch = {
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_task_by_title)
                        )
                    }, leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }, trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                text = ""
                                expanded = false
                                searchResult = emptyList()
                            })
                    })
            }) {
            Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                searchResult.forEach { resultText ->
                    ListItem(
                        headlineContent = { Text(text = resultText) },
                        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                text = resultText
                                expanded = false
                                onResultClick(text)
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp))
                }
            }
        }
    }
}

