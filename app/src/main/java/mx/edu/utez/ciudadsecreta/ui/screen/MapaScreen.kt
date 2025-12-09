package mx.edu.utez.ciudadsecreta.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

// Esto permite usar la API de permisos de Accompanist
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val context = LocalContext.current
    // 1. Manejador de permisos de Compose
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Estados del ViewModel
    val puntos by viewModel.puntos.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val initialLocation by viewModel.userInitialLocation.collectAsState() // Nueva ubicación del usuario

    // --- Lógica de permisos y localización (Se ejecuta cuando el permiso cambia) ---
    // Usamos locationPermissionState.status para observar el cambio de estado
    LaunchedEffect(locationPermissionState.status) {

        // Solo ejecutar la lógica de ubicación si aún no se ha centrado el mapa
        if (initialLocation == null) {

            val status = locationPermissionState.status

            if (status.isGranted) { // Reemplaza hasPermission
                // Permiso concedido: Obtener la última ubicación
                getLastLocation(context) { location ->
                    if (location != null) {
                        viewModel.setInitialLocation(location.latitude, location.longitude)
                    } else {
                        // No hay ubicación conocida o error, usar defecto
                        viewModel.setDefaultLocation()
                    }
                }
            } else if (status.shouldShowRationale) {
                // Permiso negado previamente (pero la app puede pedirlo de nuevo)
                // Se puede mostrar un mensaje antes de pedir permiso. Por ahora, solo usamos el por defecto.
                viewModel.setDefaultLocation()
                Toast.makeText(context, "Experiencia afectada: Ubicación no disponible.", Toast.LENGTH_LONG).show()
            } else {
                // Es la primera vez que se renderiza, o fue negado permanentemente
                // Se lanza la solicitud si es la primera vez (handled by Accompanist internally)
                locationPermissionState.launchPermissionRequest()
            }
        }
    }

    // Ubicación de centrado: Usa la ubicación del usuario si está disponible, si no, usa un punto temporal
    val centerPoint = initialLocation ?: GeoPoint(19.4326, -99.1332) // CDMX como fallback temporal

    // Configuración OSMdroid
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                // Centramos con el GeoPoint actual (será el fallback o la ubicación real)
                controller.setCenter(centerPoint)
            }
        },
        update = { map ->

            // Si la ubicación inicial es la real del usuario, animamos el centro del mapa
            if (map.getMapCenter() != centerPoint) {
                map.controller.animateTo(centerPoint, 15.0, 1000L)
            }

            // Punto 1: Definición y manejo de eventos (MapEventsOverlay)
            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {

                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p == null) return false

                    val markerTapped = puntos.firstOrNull { punto ->
                        val dist = GeoPoint(p.latitude, p.longitude)
                            .distanceToAsDouble(GeoPoint(punto.lat, punto.lon))
                        dist < 20
                    }

                    if (markerTapped != null) {
                        viewModel.abrirRumor(markerTapped)
                        return true
                    }
                    return false
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    if (p != null) {
                        viewModel.prepararNuevoRumor(
                            lat = p.latitude,
                            lon = p.longitude
                        )
                    }
                    return true
                }
            })

            // Punto 2: Limpieza de Overlays y adición de la capa de eventos
            map.overlays.clear()
            map.overlays.add(eventsOverlay)

            // Punto 3: Carga y asignación del icono del marcador
            val rumorIcon = ContextCompat.getDrawable(map.context, R.drawable.rumor_icon)

            puntos.forEach { punto ->
                val marker = Marker(map).apply {
                    position = GeoPoint(punto.lat, punto.lon)
                    title = punto.mensaje

                    // **CORRECCIÓN CLAVE:** Asigna el icono cargado directamente.
                    rumorIcon?.let {
                        this.icon = it
                    }

                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                map.overlays.add(marker)
            }

            // Punto 4: Refresca el mapa
            map.invalidate()
        }
    )

    // Diálogos de la interfaz de usuario (Dejados como estaban)

    if (uiState.currentDialog == DialogType.ADD) {
        DialogAgregarRumor(
            onDismiss = { viewModel.cerrarDialogos() },
            onSave = { texto ->
                viewModel.guardarRumor(texto)
            }
        )
    }

    if (uiState.currentDialog == DialogType.VIEW || uiState.currentDialog == DialogType.EDIT) {
        DialogRumorScreen(
            punto = uiState.puntoSeleccionado!!,
            mensajeActual = uiState.mensaje,
            usuarioActual = "Secreto",

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


// --- FUNCIONES AUXILIARES DE UBICACIÓN ---

//Obtiene la última ubicación conocida usando Fused Location Provider.

fun getLastLocation(context: Context, onLocationResult: (Location?) -> Unit) {

    // Verificación de permiso redundante pero segura
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        onLocationResult(null)
        return
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnCompleteListener { task ->
        if (task.isSuccessful && task.result != null) {
            onLocationResult(task.result)
        } else {
            // No se pudo obtener la ubicación
            onLocationResult(null)
        }
    }
}