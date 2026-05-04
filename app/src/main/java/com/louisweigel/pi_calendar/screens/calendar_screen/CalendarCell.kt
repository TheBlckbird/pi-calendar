package com.louisweigel.pi_calendar.screens.calendar_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CalendarCell(
    text: String,
    shape: Shape,
    isToday: Boolean,
    entries: List<Triple<Int?, String, Color>>,
    isThisMonth: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        {},
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp),

        shape = shape,

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),

        contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
    ) {
        Column(modifier = Modifier.align(Alignment.Top)) {
            val textModifier = if (isToday) {
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .width(28.dp)
            } else {
                Modifier
            }

            val textColor = if (isToday) {
                MaterialTheme.colorScheme.onPrimary
            } else if (!isThisMonth) {
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = textModifier.fillMaxWidth(),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                for (entry in entries) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .height(15.dp)
                            .background(entry.component3())
                            .padding(4.dp, 1.dp, 0.dp, 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            if (entry.component1() != null) {
                                Icon(
                                    painter = painterResource(entry.component1()!!),
                                    contentDescription = null
                                )

                                Spacer(Modifier.width(2.dp))
                            }

                            Text(
                                entry.component2(),
                                fontSize = 11.sp,
                                softWrap = false,
                                lineHeight = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}