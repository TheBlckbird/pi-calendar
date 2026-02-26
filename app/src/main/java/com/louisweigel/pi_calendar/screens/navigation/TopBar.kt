package com.louisweigel.pi_calendar.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onDrawerOpen: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },

        title = {
            TextButton(
                {},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    "Februar",
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown öffnen"
                )
            }
        },

        actions = {
            IconButton({}) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Suche",
                )
            }

            IconButton({}) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Heute fokussieren"
                )
            }
        }
    )
}