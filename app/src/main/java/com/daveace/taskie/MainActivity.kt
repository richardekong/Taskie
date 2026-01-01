package com.daveace.taskie

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.daveace.taskie.api.RetrofitInstance
import com.daveace.taskie.api.TaskService
import com.daveace.taskie.api.Tasks
import com.daveace.taskie.screen.MainScreen
import com.daveace.taskie.ui.theme.TaskieTheme
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun Preview() {
    TaskieTheme {
        MainScreen(modifier = Modifier)
    }
}

