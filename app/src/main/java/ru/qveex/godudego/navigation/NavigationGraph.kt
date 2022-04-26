package ru.qveex.godudego.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import ru.qveex.godudego.presentation.screens.details.DetailsScreen
import ru.qveex.godudego.presentation.screens.map.MapScreen
import ru.qveex.godudego.presentation.screens.statistic.StatsScreen
import ru.qveex.godudego.utils.Constants.STAT_ARGUMENT_KEY

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Map.route
    ) {

        composable(
            route = Screen.Map.route,
            /*enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) },*/
        ) {
            MapScreen(navController = navController)
        }

        composable(
            route = Screen.Statistics.route,
            /*enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) },*/
        ) {
            StatsScreen(navController = navController)
        }

        composable(
            route = Screen.StatDetails.route,
            /*enterTransition = { fadeIn(animationSpec = tween(600)) },
            exitTransition = { fadeOut(animationSpec = tween(600)) },*/
            arguments = listOf(navArgument(STAT_ARGUMENT_KEY) { type = NavType.IntType })
        ) {
            DetailsScreen(navController = navController)
        }
    }
}