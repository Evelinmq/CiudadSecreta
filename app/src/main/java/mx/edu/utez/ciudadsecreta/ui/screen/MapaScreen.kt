package mx.edu.utez.ciudadsecreta.ui.screen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import mx.edu.utez.ciudadsecreta.R
import mx.edu.utez.ciudadsecreta.data.model.DialogType
import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreen(viewModel: MapViewModel) {

    val context = LocalContext.current

    // Configuración OSMdroid (para mantener)
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName

    val puntos by viewModel.puntos.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                // Usamos la ubicación inicial más común para México
                controller.setCenter(GeoPoint(19.4326, -99.1332))
            }
        },
        update = { map ->
            // Punto 1: Definición y manejo de eventos (MapEventsOverlay)
            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                // Manejar toque simple -> seleccionamos el marcador
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p == null) return false

                    val markerTapped = puntos.firstOrNull { punto ->
                        val dist = GeoPoint(p.latitude, p.longitude)
                            .distanceToAsDouble(GeoPoint(punto.lat, punto.lon))
                        // Usar una distancia de umbral para determinar si se tocó un marcador
                        dist < 20
                    }

                    if (markerTapped != null) {
                        viewModel.abrirRumor(markerTapped)
                        return true
                    }
                    return false
                }

                // Manejar toque largo (para agregar nuevo rumor)
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
            // Limpiar todos los overlays, incluyendo marcadores antiguos.
            // Sirve para que al actualizar un rumor no se cree un marcador nuevo
            map.overlays.clear()
            // Añadir la capa de eventos primero
            map.overlays.add(eventsOverlay)

            // Punto 3: Carga y asignación del icono del marcador

            // Usamos ContextCompat para cargar el Drawable de forma segura.
            // Esto asegura que la imagen se cargue correctamente en el contexto del mapa.
            val rumorIcon = ContextCompat.getDrawable(map.context, R.drawable.rumor_icon)

            puntos.forEach { punto ->
                val marker = Marker(map).apply {
                    position = GeoPoint(punto.lat, punto.lon)
                    title = punto.mensaje

                    // Asigna el icono cargado directamente.
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

    // Diálogos de la interfaz de usuario

    if (uiState.currentDialog == DialogType.ADD) {
        DialogAgregarRumor(
            onDismiss = { viewModel.cerrarDialogos() },
            onSave = { texto ->
                viewModel.guardarRumor(texto) // Usa la función simplificada en el ViewModel
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