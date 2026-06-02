package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarCell(
    text: String,
    shape: Shape,
    isToday: Boolean,
    entries: List<CalendarEntryState>,
    isThisMonth: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val textColor = when {
                isToday -> MaterialTheme.colorScheme.onPrimary
                !isThisMonth -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.onBackground
            }

            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = if (isToday) {
                    Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .width(28.dp)
                } else Modifier,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                entries.take(5).forEach { entry ->
                    EntryRow(entry)
                }
            }
        }
    }
}


@Composable
private fun EntryRow(entry: CalendarEntryState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .background(entry.color, RoundedCornerShape(2.dp))
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (entry.icon != null) {
            Icon(
                painter = painterResource(entry.icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(11.dp)
            )
            Spacer(Modifier.width(2.dp))
        }

        val textStyle = if (entry.isStrikeThrough) {
            TextStyle(
                textDecoration = TextDecoration.LineThrough
            )
        } else {
            TextStyle()
        }

        Text(
            text = entry.content(),
            fontSize = 10.sp,
            color = Color.White,
            maxLines = 1,
            softWrap = false,
            lineHeight = 10.sp,
            style = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
