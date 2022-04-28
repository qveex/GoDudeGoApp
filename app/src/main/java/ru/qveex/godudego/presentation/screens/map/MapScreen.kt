package ru.qveex.godudego.presentation.screens.map

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import ru.qveex.godudego.R


@Composable
fun MapScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel = hiltViewModel()
) {

    val place1 = LatLng(59.984950, 30.343730)
    val between1 = LatLng(59.984412, 30.335288)
    val place2 = LatLng(59.990810, 30.333247)
    val group = LatLngBounds.Builder()
        .include(place1)
        .include(place2)
        .build()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(group.center, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current, R.raw.map_dark_theme
            ),
            latLngBoundsForCameraTarget = group,
            minZoomPreference = 14f,
            isMyLocationEnabled = true
        ),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings()
    ) {
        Marker(
            position = place1,
            title = "start",
            snippet = "Marker in start"
        )
        Marker(
            position = place2,
            title = "end",
            snippet = "Marker in end"
        )

        Polyline(
            points = listOf(place1, between1, place2),
            color = Color.Red,
            clickable = true
        )
    }
}