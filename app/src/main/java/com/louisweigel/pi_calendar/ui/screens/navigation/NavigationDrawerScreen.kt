package com.louisweigel.pi_calendar.ui.screens.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar

/**
 * The sidebar including the calendars and help link
 *
 * @param[calendars] All calendars that should be shown and togglable
 * @param[onCalendarToggle]
 */
@Composable
fun NavigationDrawerScreen(
    calendars: List<Calendar>,
    onCalendarToggle: (Calendar, Boolean) -> Unit,
    openCalendarManager: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.app_name),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            CalendarSectionTitle()

            for (calendar in calendars) {
                NavigationDrawerItem(
                    label = {
                        CalendarMenuItem(
                            calendar.name,
                            calendar.color,
                            calendar.isShown
                        ) { newState ->
                            onCalendarToggle(calendar, newState)
                        }
                    },
                    selected = false,
                    onClick = {
                        onCalendarToggle(calendar, !calendar.isShown)
                    }
                )
            }

            NavigationDrawerItem(
                label = { Text(stringResource(R.string.calendarManager)) },
                selected = false,
                icon = {
                    Icon(
                        painterResource(R.drawable.edit_calendar_24px),
                        null,
                    )
                },
                onClick = openCalendarManager
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            /*NavigationDrawerItem(
                label = { Text(stringResource(R.string.settings)) },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.settings_24px),
                        contentDescription = null
                    )
                },
                badge = { Text("20") }, // Placeholder
                onClick = { /* Handle click */ }
            )*/
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.help_feedback)) },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.help_24px),
                        contentDescription = null
                    )
                },
                onClick = {
                    uriHandler.openUri("https://github.com/TheBlckbird/softwareengineering/issues/new")
                },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CalendarSectionTitle() {
    Text(
        stringResource(R.string.kalender),
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
