package mx.itson.cheemstour.interfaces

import mx.itson.cheemstour.entities.Trip
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CheemsAPI {

    /**
     * Obtiene la lista de viajes (Trips) disponibles.
     * @return Call con una lista de objetos Trip.
     */
    @GET("trips")
    fun getTrips(): Call<List<Trip>>

    /**
     * Guarda un nuevo viaje enviando un objeto Trip en el cuerpo de la petición.
     * @param trip Objeto Trip que se desea guardar.
     * @return Call con un Boolean que indica si fue exitoso.
     */
    @POST("trips")
    fun saveTrip(@Body trip: Trip): Call<Boolean>

    /**
     * Actualiza un viaje existente identificado por su id.
     * @param id Identificador del viaje a actualizar.
     * @param trip Objeto Trip con los datos actualizados.
     * @return Call con un Boolean que indica si fue exitoso.
     */
    @PUT("trips/{id}")
    fun updateTrip(@Path("id") id: Int?, @Body trip: Trip): Call<Boolean>

    /**
     * Elimina un viaje identificado por su id.
     * @param id Identificador del viaje a eliminar.
     * @return Call con un Boolean que indica si fue exitoso.
     */
    @DELETE("trips/{id}")
    fun deleteTrip(@Path("id") id: Int): Call<Boolean>
}
