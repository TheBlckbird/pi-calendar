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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.screens.MonthSelection

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onDrawerOpen: () -> Unit,
    onMonthSelectionOpen: () -> Unit,
    selectedMonth: MonthSelection,
    onTodayClicked: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Icon(
                    painter = painterResource(R.drawable.menu_24px),
                    contentDescription = "Menu"
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),

        title = {
            TextButton(
                onMonthSelectionOpen,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    selectedMonth.toString(),
                    style = MaterialTheme.typography.headlineSmall,
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
                    painter = painterResource(R.drawable.search_24px),
                    contentDescription = "Suche",
                )
            }

            IconButton(onTodayClicked) {
                Icon(
                    painter = painterResource(R.drawable.today_24px),
                    contentDescription = "Heute fokussieren"
                )
            }
        }
    )
}