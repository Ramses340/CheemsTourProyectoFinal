package mx.itson.cheemstour.interfaces

import mx.itson.cheemstour.entities.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para las llamadas HTTP a la API del clima
 * usando Retrofit.
 */
interface weatherAPI {

    /**
     * Obtiene el clima actual para una ubicación dada por latitud y longitud.
     * @param lat Latitud de la ubicación.
     * @param lon Longitud de la ubicación.
     * @param appid API key para autenticación en el servicio de clima.
     * @return Call con un objeto Weather que contiene los datos del clima.
     */
    @GET("weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Call<Weather>
}
