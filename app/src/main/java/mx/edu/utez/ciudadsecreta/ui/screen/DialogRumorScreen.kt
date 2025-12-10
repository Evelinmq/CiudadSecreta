package mx.edu.utez.ciudadsecreta.ui.screen // Asegúrate de que este sea el paquete correcto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

@Composable
fun DialogRumorScreen(
    punto: PuntoMarcado,
    usuarioActual: String,
    onGuardar: (String) -> Unit,
    onEliminar: () -> Unit,
    onDismiss: () -> Unit
) {
    // 1. Determina la autoría
    val esAutor = punto.autor == usuarioActual

    // 2. Estados locales para el diálogo
    var texto by remember { mutableStateOf(punto.mensaje) }
    var editando by remember { mutableStateOf(false) }

    // 3. Lógica de activación del botón Guardar
    val textoHaCambiado = texto.isNotBlank() && texto != punto.mensaje

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editando) "Editar Rumor"
                else "Publicado por ${punto.autor}"
            )
        },
        text = {
            Column {
                if (editando) {
                    // MODO EDICIÓN
                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it },
                        label = { Text("Nuevo mensaje del rumor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // MODO VISTA
                    Text(punto.mensaje)
                }
            }
        },

        // --- Botón de Confirmación (Guardar / Editar) ---
        confirmButton = {
            if (esAutor) {
                if (editando) {
                    // MODO EDICIÓN: Botón Guardar
                    Button(
                        onClick = { onGuardar(texto) },
                        // Solo activo si el texto es válido y ha cambiado
                        enabled = textoHaCambiado
                    ) {
                        Text("Guardar")
                    }
                } else {
                    // MODO VISTA: Botón Editar
                    Button(onClick = { editando = true }) {
                        Text("Editar")
                    }
                }
            }
        },

        // --- Botón de Descarte (Cancelar / Eliminar / Cerrar) ---
        dismissButton = {
            if (esAutor) {
                OutlinedButton(
                    onClick = {
                        if (editando) {
                            // 🟢 ACCIÓN: Al cancelar, revertir el texto y salir del modo edición
                            texto = punto.mensaje
                            editando = false
                        } else {
                            // MODO VISTA: Eliminar (ViewModel se encargará de cerrar el diálogo)
                            onEliminar()
                        }
                    },
                    colors = if (!editando) ButtonDefaults.buttonColors(
                        // Color de advertencia para una acción destructiva
                        containerColor = MaterialTheme.colorScheme.error
                    ) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(if (editando) "Cancelar" else "Eliminar")
                }
            } else {
                // Si NO es autor: Botón Cerrar
                Button(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}
