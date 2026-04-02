package com.example.tracking_steps.nav

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Goals : Screen

    @Serializable
    data object Settings : Screen
}