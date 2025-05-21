package mx.itson.cheemstour.entities

import java.io.Serializable

/** Representa un viaje con sus datos principales. Serializable para pasar entre actividades */
class Trip : Serializable {
    var id: Int? = null      // ID único del viaje (puede ser nulo)
    var name: String? = null // Nombre del viaje
    var city: String? = null // Ciudad destino
    var country: String? = null // País destino
    var latitude: Double = 0.0 // Latitud
    var longitude: Double = 0.0 // Longitud

    constructor()

    constructor(id: Int?, name: String?, city: String?, country: String?, latitude: Double, longitude: Double) {
        this.id = id
        this.name = name
        this.city = city
        this.country = country
        this.latitude = latitude
        this.longitude = longitude
    }
}
