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
fun BundleScreen(navController: NavController) {
    var bundleType by remember { mutableStateOf("elliptic") }
    var pX by remember { mutableStateOf(0.2f) }
    var pY by remember { mutableStateOf(0.3f) }
    var angle by remember { mutableStateOf(45f) }
    var distance by remember { mutableStateOf(0.5f) }
    var showCurve by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пучки прямых") },
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
                    "2D Visualization\n(Canvas)",
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
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Тип пучка")
                    BundleTypeSelector(
                        selected = bundleType,
                        onSelect = { bundleType = it }
                    )

                    when (bundleType) {
                        "elliptic" -> {
                            SliderWithLabel(
                                label = "Центр X",
                                value = pX,
                                onValueChange = { pX = it },
                                valueRange = -0.9f..0.9f
                            )
                            SliderWithLabel(
                                label = "Центр Y",
                                value = pY,
                                onValueChange = { pY = it },
                                valueRange = -0.9f..0.9f
                            )
                        }
                        "parabolic" -> {
                            SliderWithLabel(
                                label = "Угол (градусы)",
                                value = angle,
                                onValueChange = { angle = it },
                                valueRange = 0f..360f
                            )
                        }
                        "hyperbolic" -> {
                            SliderWithLabel(
                                label = "Угол (градусы)",
                                value = angle,
                                onValueChange = { angle = it },
                                valueRange = 0f..180f
                            )
                            SliderWithLabel(
                                label = "Смещение",
                                value = distance,
                                onValueChange = { distance = it },
                                valueRange = 0f..0.95f
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Показать кривую")
                        Switch(
                            checked = showCurve,
                            onCheckedChange = { showCurve = it }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BundleTypeSelector(selected: String, onSelect: (String) -> Unit) {
    Column {
        RadioButtonOption(
            text = "Эллиптический (пересекающиеся)",
            selected = selected == "elliptic",
            onClick = { onSelect("elliptic") }
        )
        RadioButtonOption(
            text = "Параболический (параллельные)",
            selected = selected == "parabolic",
            onClick = { onSelect("parabolic") }
        )
        RadioButtonOption(
            text = "Гиперболический (расходящиеся)",
            selected = selected == "hyperbolic",
            onClick = { onSelect("hyperbolic") }
        )
    }
}

@Composable
fun RadioButtonOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text, modifier = Modifier.weight(1f))
    }
}