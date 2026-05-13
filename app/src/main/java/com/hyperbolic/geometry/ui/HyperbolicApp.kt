package com.hyperbolic.geometry.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperbolic.geometry.ui.screens.*

@Composable
fun HyperbolicApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            MenuScreen(navController = navController)
        }
        composable("sphere") {
            SphereScreen(navController = navController)
        }
        composable("horosphere") {
            HorosphereScreen(navController = navController)
        }
        composable("equidistant") {
            EquidistantScreen(navController = navController)
        }
        composable("bundle") {
            BundleScreen(navController = navController)
        }
    }
}

@Composable
fun TopAppBarWithBack(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.Home, contentDescription = "Back")
            }
        }
    )
}