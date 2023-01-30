package com.example.notificationsinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.notificationsinandroid.navigation.SetupNavGraph
import com.example.notificationsinandroid.ui.theme.NotificationsInAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationsInAndroidTheme {
                SetupNavGraph(navController = rememberNavController())
            }
        }
    }
}

