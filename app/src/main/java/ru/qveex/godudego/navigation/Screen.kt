package ru.qveex.godudego.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import ru.qveex.godudego.utils.Constants.STAT_ARGUMENT_KEY


sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Map: Screen(
        route = "map",
        title = "Go",
        icon = Icons.Outlined.DirectionsRun
    )

    object Statistics: Screen(
        route = "statistics",
        title = "Statistics",
        icon = Icons.Outlined.QueryStats
    )

    object StatDetails: Screen(
        route = "StatDetails/{$STAT_ARGUMENT_KEY}",
        title = "StatDetails",
        icon = Icons.Outlined.Details
    ) {
        fun passId(statId: Int) = "StatDetails/$statId"
    }
}
