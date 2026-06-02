package com.louisweigel.pi_calendar.ui.screens.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pi_calendar_kmp.shared.generated.resources.Res
import pi_calendar_kmp.shared.generated.resources.addEntriesFab_erinnerung
import pi_calendar_kmp.shared.generated.resources.addEntriesFab_geburtstag
import pi_calendar_kmp.shared.generated.resources.addEntriesFab_termin
import pi_calendar_kmp.shared.generated.resources.add_2_24px
import pi_calendar_kmp.shared.generated.resources.cake_24px
import pi_calendar_kmp.shared.generated.resources.event_24px
import pi_calendar_kmp.shared.generated.resources.task_alt_24px


@Composable
fun AddEventsMenu(
    onNewEvent: () -> Unit,
    onNewBirthday: () -> Unit,
    onNewReminder: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val fabRadius = if (isExpanded) {
        28.dp
    } else {
        16.dp // makes it a circle, its size is 56dp
    }
    val fabCornerRadius = animateDpAsState(targetValue = fabRadius)

    val fabRotation = if (isExpanded) {
        45f
    } else {
        0f
    }
    val fabIconRotation = animateFloatAsState(fabRotation)

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FabMenuEntry(0, isExpanded,
                stringResource(Res.string.addEntriesFab_geburtstag), {
                onNewBirthday()
                isExpanded = false
            }) {
                Icon(
                    painter = painterResource(Res.drawable.cake_24px),
                    contentDescription = null
                )
            }

            FabMenuEntry(1, isExpanded,
                stringResource(Res.string.addEntriesFab_erinnerung), {
                onNewReminder()
                isExpanded = false
            }) {
                Icon(
                    painter = painterResource(Res.drawable.task_alt_24px),
                    contentDescription = null
                )
            }

            FabMenuEntry(2, isExpanded, stringResource(Res.string.addEntriesFab_termin), {
                onNewEvent()
                isExpanded = false
            }) {
                Icon(
                    painter = painterResource(Res.drawable.event_24px),
                    contentDescription = null
                )
            }
        }

        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            shape = RoundedCornerShape(fabCornerRadius.value),
            contentColor = if (isExpanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_2_24px),
                    contentDescription = "Write or Reflect Today's Reflection",
                    modifier = Modifier.rotate(fabIconRotation.value)
                )
            }
        }
    }
}

@Composable
fun FabMenuEntry(
    index: Int,
    isExpanded: Boolean,
    title: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInVertically(initialOffsetY = { it * (index + 1) }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it * (index + 1) }) + fadeOut()
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.height(56.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
