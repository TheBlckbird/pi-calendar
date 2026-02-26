package com.louisweigel.pi_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.louisweigel.pi_calendar.screens.CounterView
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddEventButtonViewModel : ViewModel() {
    private val _isAddEventMenuOpen = MutableStateFlow(false)
    val isAddEventMenuOpen: StateFlow<Boolean> = _isAddEventMenuOpen.asStateFlow()

    fun toggleMenu() {
        _isAddEventMenuOpen.value = !_isAddEventMenuOpen.value
    }

    fun closeMenu() {
        _isAddEventMenuOpen.value = false
    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicalendarTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton({}) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            },

                            title = {
                                TextButton(
                                    {},
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp),
                                    colors = ButtonDefaults.textButtonColors().copy(
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(
                                        "Februar",
                                        style = MaterialTheme.typography.titleLarge
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
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Suche",
                                    )
                                }

                                IconButton({}) {
                                    Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = "Heute fokussieren"
                                    )
                                }
                            }
                        )
                    },

                    floatingActionButton = {
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
                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = slideInVertically(initialOffsetY = { it * 1 }) + fadeIn(),
                                    exit = slideOutVertically(targetOffsetY = { it * 1 }) + fadeOut()
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
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = null
                                            )
                                            Text(
                                                "Geburtstag",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                }

                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = slideInVertically(initialOffsetY = { it * 2 }) + fadeIn(),
                                    exit = slideOutVertically(targetOffsetY = { it * 2 }) + fadeOut()
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
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = null
                                            )
                                            Text(
                                                "Erinnerung",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                }

                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = slideInVertically(initialOffsetY = { it * 3 }) + fadeIn(),
                                    exit = slideOutVertically(targetOffsetY = { it * 3 }) + fadeOut()
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
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = null
                                            )
                                            Text(
                                                "Termin",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
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

                ) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier) {
    Column(modifier) {
        CounterView()

        LazyColumn(Modifier.fillMaxSize()) {
            items(10000000) { index ->
                Text(
                    text = "Number ${index + 1}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun AddEventButton(isMenuOpen: Boolean, onClick: () -> Unit) {
    FloatingActionButton(onClick) {
        Icon(
            if (isMenuOpen) Icons.Default.Close else Icons.Default.Add,
            "Neues Event erstellen"
        )
    }
}

@Composable
fun FabMenuItems(
    isMenuOpen: Boolean,
    menuItems: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(bottom = 72.dp)
    ) {
        menuItems.forEachIndexed { index, item ->
            AnimatedVisibility(
                visible = isMenuOpen,
                enter = slideInVertically(initialOffsetY = { it * (index + 1) }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it * (index + 1) }) + fadeOut()
            ) {
                FloatingActionButton(onClick = item.second) {
                    Text(item.first, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun FabMenuScreen(viewModel: AddEventButtonViewModel = viewModel()) {
    val isMenuOpen by viewModel.isAddEventMenuOpen.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        FabMenuItems(
            isMenuOpen = isMenuOpen,
            menuItems = listOf(
                "Edit" to { viewModel.closeMenu(); /* Handle edit */ },
                "Share" to { viewModel.closeMenu(); /* Handle share */ }
            )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddEventButton(isMenuOpen = isMenuOpen) {
                viewModel.toggleMenu()
            }
        }
    }
}
