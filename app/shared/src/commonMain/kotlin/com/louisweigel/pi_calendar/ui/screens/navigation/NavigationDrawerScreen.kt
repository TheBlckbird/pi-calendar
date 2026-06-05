package com.louisweigel.pi_calendar.ui.screens.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.core.Calendar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pi_calendar.app.shared.generated.resources.*

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
                stringResource(Res.string.app_name),
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
                label = { Text(stringResource(Res.string.calendarManager)) },
                selected = false,
                icon = {
                    Icon(
                        painterResource(Res.drawable.edit_calendar_24px),
                        null,
                    )
                },
                onClick = openCalendarManager
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            /*NavigationDrawerItem(
                label = { Text(stringResource(Res.string.settings)) },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.settings_24px),
                        contentDescription = null
                    )
                },
                badge = { Text("20") }, // Placeholder
                onClick = { /* Handle click */ }
            )*/
            NavigationDrawerItem(
                label = { Text(stringResource(Res.string.help_feedback)) },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.help_24px),
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
        stringResource(Res.string.kalender),
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
