package mx.itson.cheemstour.utils

import com.google.gson.GsonBuilder
import mx.itson.cheemstour.interfaces.weatherAPI
import mx.itson.cheemstour.interfaces.CheemsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton para proveer instancias configuradas de Retrofit
 * para consumir APIs REST tanto local como externa (OpenWeather).
 */
object RetrofitUtil {

    /**
     * Obtiene la instancia de la API local para CRUD de viajes.
     * @return CheemsAPI implementada por Retrofit.
     */
    fun getApi(): CheemsAPI? {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.125:5000") // URL base de la API local
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(CheemsAPI::class.java)
    }

    /**
     * Obtiene la instancia de la API externa OpenWeather para el clima.
     * @return weatherAPI implementada por Retrofit.
     */
    fun getApiWeather(): weatherAPI? {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/") // URL base para clima
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(weatherAPI::class.java)
    }
}
