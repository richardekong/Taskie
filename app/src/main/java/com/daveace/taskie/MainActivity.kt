package com.daveace.taskie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daveace.taskie.screen.TasksScreen
import com.daveace.taskie.ui.theme.TaskieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    SignUpScreen(modifier = Modifier)
                    TasksScreen(modifier = Modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TaskieTheme {
//        SignUpScreen(modifier = Modifier)
        TasksScreen(modifier = Modifier)
    }
}