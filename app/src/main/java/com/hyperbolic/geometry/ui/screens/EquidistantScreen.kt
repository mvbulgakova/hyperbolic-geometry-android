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
fun EquidistantScreen(navController: NavController) {
    var zLevel by remember { mutableStateOf(0.0f) }
    var distance by remember { mutableStateOf(2.5f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Эквидистантная поверхность") },
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
                    Text("Параметры эквидистанты")

                    SliderWithLabel(
                        label = "Z уровень базовой плоскости",
                        value = zLevel,
                        onValueChange = { zLevel = it },
                        valueRange = -0.95f..0.95f
                    )

                    SliderWithLabel(
                        label = "Гиперболическое расстояние",
                        value = distance,
                        onValueChange = { distance = it },
                        valueRange = 0f..3.0f
                    )
                }
            }
        }
    }
}