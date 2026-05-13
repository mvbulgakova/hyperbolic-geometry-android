package com.hyperbolic.geometry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hyperbolic.geometry.geometry.HyperbolicSphere

@Composable
fun SphereScreen(navController: NavController) {
    var centerX by remember { mutableStateOf(0.2f) }
    var centerY by remember { mutableStateOf(-0.1f) }
    var centerZ by remember { mutableStateOf(0.3f) }
    var radius by remember { mutableStateOf(0.4f) }
    var showAxes by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Гиперболическая сфера") },
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
            // 3D Visualization area
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

            // Control Panel
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
                    Text("Параметры сферы")

                    SliderWithLabel(
                        label = "Центр X",
                        value = centerX,
                        onValueChange = { centerX = it },
                        valueRange = -0.6f..0.6f
                    )

                    SliderWithLabel(
                        label = "Центр Y",
                        value = centerY,
                        onValueChange = { centerY = it },
                        valueRange = -0.6f..0.6f
                    )

                    SliderWithLabel(
                        label = "Центр Z",
                        value = centerZ,
                        onValueChange = { centerZ = it },
                        valueRange = -0.6f..0.6f
                    )

                    SliderWithLabel(
                        label = "Радиус",
                        value = radius,
                        onValueChange = { radius = it },
                        valueRange = 0.05f..0.8f
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Показать оси")
                        Switch(
                            checked = showAxes,
                            onCheckedChange = { showAxes = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SliderWithLabel(label: String, value: Float, onValueChange: (Float) -> Unit, valueRange: ClosedFloatingPointRange<Float>) {
    Column {
        Text("$label: ${'%.2f'.format(value)}")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}