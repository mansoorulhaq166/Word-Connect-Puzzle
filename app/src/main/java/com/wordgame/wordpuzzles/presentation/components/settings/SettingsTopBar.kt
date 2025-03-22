package com.wordgame.wordpuzzles.presentation.components.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.presentation.theme.alfa_slab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Settings",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
                    .copy(fontSize = 22.sp, fontFamily = alfa_slab)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_back),
                    contentDescription = "Back",
                    modifier = Modifier.size(36.dp), // Adjust size as needed
                    contentScale = ContentScale.Fit
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Color.White
        )
    )
}