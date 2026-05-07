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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R

@Composable
fun AddEventsMenu(
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    onClose: () -> Unit,
    onNewEvent: () -> Unit,
    onNewBirthday: () -> Unit,
    onNewReminder: () -> Unit,
) {
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
                stringResource(R.string.addEntriesFab_geburtstag), {
                onNewBirthday()
                onClose()
            }) {
                Icon(
                    painter = painterResource(R.drawable.cake_24px),
                    contentDescription = null
                )
            }

            FabMenuEntry(1, isExpanded,
                stringResource(R.string.addEntriesFab_erinnerung), {
                onNewReminder()
                onClose()
            }) {
                Icon(
                    painter = painterResource(R.drawable.task_alt_24px),
                    contentDescription = null
                )
            }

            FabMenuEntry(2, isExpanded, stringResource(R.string.addEntriesFab_termin), {
                onNewEvent()
                onClose()
            }) {
                Icon(
                    painter = painterResource(R.drawable.event_24px),
                    contentDescription = null
                )
            }
        }

        FloatingActionButton(
            onClick = onToggleExpanded,
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
                    imageVector = Icons.Default.Add,
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