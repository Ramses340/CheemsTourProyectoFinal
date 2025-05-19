package mx.itson.cheemstour.interfaces


import mx.itson.cheemstour.entities.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherAPI {

    @GET("weather")

    fun getWeather(@Query("lat") lat: Double,
                   @Query("lon") lon: Double,
                   @Query("appid") appid: String): Call<Weather>

}