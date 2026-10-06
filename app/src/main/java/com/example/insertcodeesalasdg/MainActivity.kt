package com.example.insertcodeesalasdg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.insertcodeesalasdg.navigation.AppNavigation
import com.example.insertcodeesalasdg.ui.theme.InserCodeTheme
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

class MainActivity : ComponentActivity() {

    private val estadoViewModel: EstadoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InserCodeTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    estadoViewModel = estadoViewModel
                )
            }
        }
    }
}