package mx.edu.utez.ciudadsecreta.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import mx.edu.utez.ciudadsecreta.R
import mx.edu.utez.ciudadsecreta.data.model.DialogType
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val puntos by viewModel.puntos.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val initialLocation by viewModel.userInitialLocation.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    // --- Lógica de permisos y localización ---
    LaunchedEffect(locationPermissionState.status) {
        if (initialLocation == null) {
            val status = locationPermissionState.status
            if (status.isGranted) {
                getLastLocation(context) { location ->
                    if (location != null) {
                        viewModel.setInitialLocation(location.latitude, location.longitude)
                    } else {
                        viewModel.setDefaultLocation()
                    }
                }
            } else if (status.shouldShowRationale) {
                viewModel.setDefaultLocation()
                Toast.makeText(
                    context,
                    "Experiencia afectada: Ubicación no disponible.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }

    val centerPoint = initialLocation ?: GeoPoint(19.4326, -99.1332)

    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    controller.setCenter(centerPoint)
                }
            },
            update = { map ->
                if (map.getMapCenter() != centerPoint) {
                    map.controller.animateTo(centerPoint, 15.0, 1000L)
                }

                val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                        if (p == null) return false
                        val markerTapped = puntos.firstOrNull { punto ->
                            GeoPoint(p.latitude, p.longitude).distanceToAsDouble(
                                GeoPoint(
                                    punto.lat,
                                    punto.lon
                                )
                            ) < 20
                        }
                        if (markerTapped != null) {
                            viewModel.abrirRumor(markerTapped)
                            return true
                        }
                        return false
                    }

                    override fun longPressHelper(p: GeoPoint?): Boolean {
                        if (p != null) {
                            viewModel.prepararNuevoRumor(lat = p.latitude, lon = p.longitude)
                        }
                        return true
                    }
                })

                map.overlays.clear()
                map.overlays.add(eventsOverlay)

                val rumorIcon = ContextCompat.getDrawable(map.context, R.drawable.rumorsito)

                puntos.forEach { punto ->
                    val marker = Marker(map).apply {
                        position = GeoPoint(punto.lat, punto.lon)
                        title = punto.mensaje
                        rumorIcon?.let { this.icon = it }
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    map.overlays.add(marker)
                }
                map.invalidate()
            }
        )

        if (isSaving) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (uiState.currentDialog == DialogType.ADD) {
        DialogAgregarRumor(
            onDismiss = { viewModel.cerrarDialogos() },
            onSave = { texto ->
                viewModel.guardarRumor(texto)
            },
            isSaving = isSaving
        )
    }

    val currentUserId = "Secreto"


    if (uiState.currentDialog == DialogType.VIEW || uiState.currentDialog == DialogType.EDIT) {
        DialogRumorScreen(
            punto = uiState.puntoSeleccionado!!,
            usuarioActual = currentUserId,

            onGuardar = { nuevo ->
                viewModel.editarRumor(nuevo)
            },

            onEliminar = {
                viewModel.borrarRumor()
            },

            onDismiss = {
                viewModel.cerrarDialogos()
            }
        )
    }
}

fun getLastLocation(context: Context, onLocationResult: (Location?) -> Unit) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onLocationResult(null)
        return
    }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnCompleteListener { task ->
        if (task.isSuccessful && task.result != null) {
            onLocationResult(task.result)
        } else {
            onLocationResult(null)
        }
    }

}
