package ru.qveex.godudego.presentation.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import ru.qveex.godudego.R


@Composable
fun MapScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel = hiltViewModel()
) {

    val spb = LatLng(59.938989, 30.315745)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(spb, 10f)
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current, R.raw.map_dark_theme
            )
        ),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(),

        ) {
        Marker(
            position = spb,
            title = "Saint-Petersburg",
            snippet = "Marker in Saint-Petersburg"
        )

    }
}