package com.louisweigel.pi_calendar.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.unit.dp

@Composable
fun AddEventsMenu() {
    var isExpanded by remember { mutableStateOf(false) }
    val fabRadius = if (isExpanded) {
        28.dp
    } else {
        16.dp // makes it a circle, its size is 56dp
    }
    val fabCornerRadius = animateDpAsState(targetValue = fabRadius)

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FabMenuEntry(0, isExpanded, "Geburtstag") {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            }

            FabMenuEntry(1, isExpanded, "Erinnerung") {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            }

            FabMenuEntry(2, isExpanded, "Termin") {
                Icon(
                    imageVector = Icons.Default.Menu,
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
                    imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = "Write or Reflect Today's Reflection",
                    modifier = if (isExpanded) Modifier.size(20.dp) else Modifier // Makes things look more natural
                )
            }
        }
    }
}

@Composable
fun FabMenuEntry(index: Int, isExpanded: Boolean, title: String, icon: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInVertically(initialOffsetY = { it * (index + 1) }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it * (index + 1) }) + fadeOut()
    ) {
        Button(
            onClick = {},
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