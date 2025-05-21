package mx.itson.cheemstour

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.entities.Weather
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que muestra un mapa con los viajes obtenidos desde una API
 * y permite ver el clima actual de cada lugar al hacer clic en un marcador.
 */
class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_map)

        // Ajusta los márgenes para evitar superposición con la barra de estado y navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Carga el fragmento del mapa
        val mapaFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    }

    /**
     * Callback cuando el mapa está listo. Configura el mapa y carga los viajes.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map?.mapType = GoogleMap.MAP_TYPE_NORMAL
            getTrips()

            // Listener para mostrar el clima al hacer clic en un marcador
            map?.setOnMarkerClickListener { marker ->
                val trip = marker.tag as? Trip
                trip?.let { getWeatherAndShowInfo(marker, it) }
                true
            }
        } catch (ex: Exception) {
            Log.e("Error loading map", ex.toString())
        }
    }

    /**
     * Obtiene la lista de viajes desde la API y agrega marcadores en el mapa.
     */
    private fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi()!!.getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                val trips = response.body().orEmpty()
                for (trip in trips) {
                    val lat = trip.latitude
                    val lon = trip.longitude
                    if (lat != null && lon != null) {
                        val latLng = LatLng(lat, lon)
                        val marker = map?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(trip.name)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheems))
                        )
                        marker?.tag = trip
                    }
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error", "Error calling trips API: ${t.message}")
            }
        })
    }

    /**
     * Obtiene el clima de una ubicación y muestra un diálogo con información
     * y color de fondo que depende de la temperatura.
     */
    private fun getWeatherAndShowInfo(marker: Marker, trip: Trip) {
        val lat = trip.latitude ?: return
        val lon = trip.longitude ?: return

        val call: Call<Weather> = RetrofitUtil.getApiWeather()!!
            .getWeather(lat, lon, "1ff7f99cc268bfd768cbf2ab5003b900")

        call.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val weather = response.body() ?: return

                // Convertir timestamp a hora local
                val localTime = java.time.Instant.ofEpochSecond(weather.dt.toLong())
                    .atOffset(java.time.ZoneOffset.ofTotalSeconds(weather.timezone))
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))

                // Inflar layout personalizado del diálogo
                val view = layoutInflater.inflate(R.layout.activity_weather_info, null)

                // Obtener temperatura en Celsius
                val tempCelsius = (weather.main?.temp ?: 273.15) - 273.15

                // Asignar datos del clima a la vista
                view.findViewById<TextView>(R.id.textLocation).text = "${getString(R.string.location_name)}: ${weather.name}"
                view.findViewById<TextView>(R.id.textTemperature).text = "${getString(R.string.temperature)}: ${"%.1f".format(tempCelsius)}°C"
                view.findViewById<TextView>(R.id.textDescription).text = "${getString(R.string.weather_description)}: ${weather.weather?.get(0)?.description}"
                view.findViewById<TextView>(R.id.textHumidity).text = "${getString(R.string.humidity)}: ${weather.main?.humidity}%"
                view.findViewById<TextView>(R.id.textWind).text = "${getString(R.string.wind)}: ${weather.wind?.speed} m/s"
                view.findViewById<TextView>(R.id.textClouds).text = "${getString(R.string.clouds)}: ${weather.clouds?.all}%"
                view.findViewById<TextView>(R.id.textLocalTime).text = "${getString(R.string.local_time)}: $localTime"


                // Cargar ícono del clima desde OpenWeatherMap usando Glide
                val iconCode = weather.weather?.get(0)?.icon
                val imageUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
                Glide.with(this@TripMapActivity)
                    .load(imageUrl)
                    .into(view.findViewById(R.id.imageWeatherIcon))

                // Cambiar el color del fondo según la temperatura
                val backgroundColor = when {
                    tempCelsius <= 10 -> getColor(R.color.cold_blue)         // Frío
                    tempCelsius in 10.0..25.0 -> getColor(R.color.mild_green) // Templado
                    else -> getColor(R.color.hot_red)                        // Calor
                }
                view.setBackgroundColor(backgroundColor)

                // Mostrar el diálogo
                androidx.appcompat.app.AlertDialog.Builder(this@TripMapActivity)
                    .setView(view)
                    .setPositiveButton("OK", null)
                    .show()
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("Error", "Weather API failed: ${t.message}")
            }
        })
    }
}
