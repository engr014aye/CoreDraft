package com.reformistsinstitute.coredraft.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.reformistsinstitute.coredraft.CoreDraftApplication
import com.reformistsinstitute.coredraft.presentation.navigation.NavGraph
import com.reformistsinstitute.coredraft.presentation.theme.CoreDraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as CoreDraftApplication).container

        setContent {
            CoreDraftTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    container = appContainer
                )
            }
        }
    }
}
