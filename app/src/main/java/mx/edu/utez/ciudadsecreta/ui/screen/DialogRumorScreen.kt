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
    val esAutor = punto.autor == usuarioActual
    var texto by remember { mutableStateOf(punto.mensaje) }
    var editando by remember { mutableStateOf(false) }

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
                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it },
                        label = { Text("Nuevo mensaje del rumor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(punto.mensaje)
                }
            }
        },
        confirmButton = {
            if (esAutor) {
                if (editando) {
                    Button(
                        onClick = { onGuardar(texto) },
                        enabled = textoHaCambiado
                    ) {
                        Text("Guardar")
                    }
                } else {
                    Button(onClick = { editando = true }) {
                        Text("Editar")
                    }
                }
            }
        },
        dismissButton = {
            if (esAutor) {
                OutlinedButton(
                    onClick = {
                        if (editando) {
                            texto = punto.mensaje
                            editando = false
                        } else {
                            onEliminar()
                        }
                    },
                    colors = if (!editando) ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ) else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(if (editando) "Cancelar" else "Eliminar")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )
}
