package com.example.tracking_steps.nav

import androidx.annotation.DrawableRes
import com.example.utility.R

enum class TopLevelDestination(
    @DrawableRes val icon: Int,
    val label: String,
    val screen: Screen,
) {
    HOME(
        icon = R.drawable.fork,
        label = "Home",
        screen = Screen.Home,
    ),
    GOALS(
        icon = R.drawable.camera,
        label = "Goals",
        screen = Screen.Goals,
    ),
    SETTINGS(
        icon = R.drawable.fork,
        label = "Settings",
        screen = Screen.Settings,
    ),
}