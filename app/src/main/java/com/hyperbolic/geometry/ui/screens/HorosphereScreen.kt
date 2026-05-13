package com.hyperbolic.geometry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HorosphereScreen(navController: NavController) {
    var phi by remember { mutableStateOf(45f) }
    var theta by remember { mutableStateOf(90f) }
    var kPrime by remember { mutableStateOf(3.0f) }
    var showGuiding by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Орисфера") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    "3D Visualization\n(OpenGL Surface)",
                    modifier = Modifier.padding(16.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Параметры орисферы")

                    SliderWithLabel(
                        label = "Долгота Phi (градусы)",
                        value = phi,
                        onValueChange = { phi = it },
                        valueRange = 0f..360f
                    )

                    SliderWithLabel(
                        label = "Широта Theta (градусы)",
                        value = theta,
                        onValueChange = { theta = it },
                        valueRange = 0f..180f
                    )

                    SliderWithLabel(
                        label = "Размер орисферы k'",
                        value = kPrime,
                        onValueChange = { kPrime = it },
                        valueRange = 0.1f..10f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Показать направляющие")
                        Switch(
                            checked = showGuiding,
                            onCheckedChange = { showGuiding = it }
                        )
                    }
                }
            }
        }
    }
}