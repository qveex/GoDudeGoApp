package ru.qveex.godudego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import ru.qveex.godudego.navigation.SetupNavGraph
import ru.qveex.godudego.presentation.screens.MainScreen
import ru.qveex.godudego.ui.theme.GoDudeGoTheme

// https://youtu.be/0rc75uR0CNs
@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoDudeGoTheme {
                MainScreen()
            }
        }
    }
}