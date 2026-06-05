package com.louisweigel.pi_calendar.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.core.toTitle
import kotlinx.datetime.YearMonth
import pi_calendar.app.shared.generated.resources.Res
import pi_calendar.app.shared.generated.resources.arrow_drop_down_24px
import pi_calendar.app.shared.generated.resources.menu_24px
import pi_calendar.app.shared.generated.resources.search_24px
import pi_calendar.app.shared.generated.resources.today_24px

@Composable
fun TopBar(
    onDrawerOpen: () -> Unit,
    onMonthSelectionOpen: () -> Unit,
    selectedYearMonth: YearMonth,
    onTodayClicked: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onDrawerOpen) {
                Icon(
                    painter = painterResource(Res.drawable.menu_24px),
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
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    selectedYearMonth.toTitle(),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Icon(
                    painter = painterResource(Res.drawable.arrow_drop_down_24px),
                    contentDescription = "Dropdown öffnen"
                )
            }
        },

        actions = {
            IconButton({}) {
                Icon(
                    painter = painterResource(Res.drawable.search_24px),
                    contentDescription = "Suche",
                )
            }

            IconButton(onTodayClicked) {
                Icon(
                    painter = painterResource(Res.drawable.today_24px),
                    contentDescription = "Heute fokussieren"
                )
            }
        }
    )
}
