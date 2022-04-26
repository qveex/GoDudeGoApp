package ru.qveex.godudego.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ru.qveex.godudego.navigation.SetupNavGraph
import ru.qveex.godudego.presentation.components.BottomNav

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    PermissionGetter()

    Scaffold(
        bottomBar = { BottomNav(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SetupNavGraph(navController = navController)
        }
    }
}


@ExperimentalPermissionsApi
@Composable
fun PermissionGetter() {

    val context = LocalContext.current
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // TODO возможно придется переделать в MainActivity обычными реквестами
    // TODO запросы на права идут до тех пор, пока состояние прав как-либо ИЗМЕНЯЕТСЯ
    // TODO + тост будет появляется каждый раз при заходе, а не при выдаче

    if (locationPermissionState.allPermissionsGranted) {
        Toast.makeText(context, "Permission granted!", Toast.LENGTH_LONG).show()
    } else {
        if (locationPermissionState.permissionRequested) {
            if (locationPermissionState.shouldShowRationale) {
                LaunchedEffect(true) {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
            } else {
                AlertDialog(
                    title = "Permissions",
                    text = "Please give the app permission to access your location",
                    onConfirm = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            val uri = Uri.fromParts("package", context.packageName, null)
                            data = uri
                        }
                        context.startActivity(intent)
                    },
                    onDismiss = { (context as? Activity)?.finish() }
                )
            }
        } else {
            LaunchedEffect(Unit) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}


@Composable
fun AlertDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    Column {
        val openDialog = remember { mutableStateOf(true) }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = title) },
                text = { Text(text = text) },
                confirmButton = {
                    Button(
                        onClick = {
                            onConfirm()
                            openDialog.value = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            onDismiss()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }


}