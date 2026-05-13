package com.hyperbolic.geometry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hyperbolic Geometry") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Модель Бельтрами-Клейна",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            MenuButton(
                title = "Гиперболическая сфера",
                description = "Интерактивная 3D-модель сферы",
                onClick = { navController.navigate("sphere") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(
                title = "Орисфера",
                description = "Модель орисферы с геодезическими",
                onClick = { navController.navigate("horosphere") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(
                title = "Эквидистантная поверхность",
                description = "Визуализация эквидистант",
                onClick = { navController.navigate("equidistant") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(
                title = "Пучки прямых",
                description = "Эллиптические и гиперболические пучки",
                onClick = { navController.navigate("bundle") }
            )
        }
    }
}

@Composable
fun MenuButton(title: String, description: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
    }
}