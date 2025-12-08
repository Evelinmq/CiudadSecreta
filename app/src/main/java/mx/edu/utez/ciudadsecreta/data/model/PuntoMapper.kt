package mx.edu.utez.ciudadsecreta.data.model

fun PuntoResponse.toPuntoMarcado(): PuntoMarcado {
    return PuntoMarcado(
        id = this.id,
        lat = this.lat,
        lon = this.lon,
        mensaje = this.mensaje,
        autor = this.autor,
        timestamp = this.timestamp
    )
}
