package com.louisweigel.pi_calendar.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

const val EnterDuration = 300
const val ExitDuration = 250

val ForwardEnter = tween<IntOffset>(
    durationMillis = EnterDuration,
    easing = androidx.compose.animation.core.FastOutSlowInEasing
)

val ForwardExit = tween<IntOffset>(
    durationMillis = ExitDuration,
    easing = androidx.compose.animation.core.FastOutLinearInEasing
)

val BackEnter = tween<IntOffset>(
    durationMillis = EnterDuration,
    easing = androidx.compose.animation.core.FastOutSlowInEasing
)

val BackExit = tween<IntOffset>(
    durationMillis = ExitDuration,
    easing = androidx.compose.animation.core.FastOutLinearInEasing
)

fun AnimatedContentTransitionScope<NavBackStackEntry>.materialForwardEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = ForwardEnter,
        initialOffset = { fullWidth -> fullWidth / 4 }
    ) + fadeIn(
        animationSpec = tween(durationMillis = EnterDuration)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.materialForwardExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = ForwardExit,
        targetOffset = { fullWidth -> -fullWidth / 10 }
    ) + fadeOut(
        animationSpec = tween(durationMillis = ExitDuration)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.materialBackEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = BackEnter,
        initialOffset = { fullWidth -> -fullWidth / 10 }
    ) + fadeIn(
        animationSpec = tween(durationMillis = EnterDuration)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.materialBackExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = BackExit,
        targetOffset = { fullWidth -> fullWidth / 4 }
    ) + fadeOut(
        animationSpec = tween(durationMillis = ExitDuration)
    )
}