// Este código debería estar en un archivo separado, por ejemplo, Dialogs.kt

package mx.edu.utez.ciudadsecreta.ui.screen // Reemplaza por tu paquete de diálogos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DialogAgregarRumor(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,

    isSaving: Boolean
) {
    val texto = rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) onDismiss() // Solo permite descartar si no está guardando
        },

        title = { Text("Nuevo Rumor") },

        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = texto.value,
                    onValueChange = { texto.value = it },
                    label = { Text("Escribe tu rumor") },
                    // Deshabilita la entrada mientras guarda
                    enabled = !isSaving
                )
                // Muestra el indicador de carga superpuesto
                if (isSaving) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        },

        confirmButton = {
            Button(
                // 🚩 CORRECCIÓN CLAVE: Deshabilita el botón mientras se guarda
                onClick = { onSave(texto.value) },
                enabled = !isSaving && texto.value.isNotBlank() // También requiere texto
            ) {
                Text(if (isSaving) "Guardando..." else "Guardar")
            }
        },

        dismissButton = {
            Button(
                onClick = onDismiss,
                enabled = !isSaving // Deshabilita el botón de cancelar mientras guarda
            ) {
                Text("Cancelar")
            }
        }
    )
}

