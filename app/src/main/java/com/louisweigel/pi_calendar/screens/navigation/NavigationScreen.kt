package com.louisweigel.pi_calendar.screens.navigation

import android.widget.CheckBox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R

@Composable
fun NavigationDrawerScreen() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(true) }

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "π Kalender",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            CalendarSectionTitle()
            NavigationDrawerItem(
                label = {
                    CalendarMenuItem(
                        "Mein Kalender",
                        MaterialTheme.colorScheme.secondary,
                        checked1
                    ) { newState -> checked1 = newState }
                },
                selected = false,
                onClick = {
                    checked1 = !checked1
                }
            )
            NavigationDrawerItem(
                label = {
                    CalendarMenuItem(
                        "Geburtstage",
                        MaterialTheme.colorScheme.tertiary,
                        checked2
                    ) { newState ->
                        checked2 = newState
                    }
                },
                selected = false,
                onClick = {
                    checked2 = !checked2
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.settings_24px),
                        contentDescription = null
                    )
                },
                badge = { Text("20") }, // Placeholder
                onClick = { /* Handle click */ }
            )
            NavigationDrawerItem(
                label = { Text("Help & feedback") },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.help_24px),
                        contentDescription = null
                    )
                },
                onClick = { /* Handle click */ },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CalendarSectionTitle() {
    Text(
        "Kalender",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun CalendarMenuItem(
    name: String,
    color: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = color)
        )
        Text(name)
    }
}