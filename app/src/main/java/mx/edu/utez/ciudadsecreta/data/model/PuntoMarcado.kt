package mx.edu.utez.ciudadsecreta.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PuntoMarcado(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lat: Double,
    val lon: Double,
    val mensaje: String,
    val autor: String,
    val timestamp: Long
)
