package mx.itson.cheemstour

import android.content.Context
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que permite al usuario crear un nuevo viaje.
 * Incluye un formulario para ingresar nombre, ciudad, país y seleccionar la ubicación en un mapa.
 */
class TripFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_form)

        // Ajusta el padding del layout principal para respetar las barras del sistema (notch, barra de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura el listener para el botón Guardar viaje
        findViewById<Button>(R.id.btn_save).setOnClickListener(this)

        // Obtiene el fragmento del mapa y solicita que se notifique cuando esté listo
        val mapaFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    }

    /**
     * Evento que se ejecuta al hacer clic en cualquier vista con listener asignado.
     * Aquí maneja el botón Guardar viaje.
     */
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_save) {
            // Obtiene los valores ingresados por el usuario en el formulario
            val name = findViewById<EditText>(R.id.txt_name).text.toString().trim()
            val city = findViewById<EditText>(R.id.txt_city).text.toString().trim()
            val country = findViewById<EditText>(R.id.txt_country).text.toString().trim()

            // Valida que los campos no estén vacíos
            if (name.isEmpty() || city.isEmpty() || country.isEmpty()) {
                // Muestra un Toast más amigable si hay campos vacíos
                Toast.makeText(this, "¡Ups! Asegúrate de llenar todos los campos ✏️", Toast.LENGTH_SHORT).show()
                vibrateShort()
            } else {
                // Vibración más intensa para confirmar guardado
                vibrateStrong()

                // Crea el objeto Trip con los datos capturados y las coordenadas seleccionadas en el mapa
                val trip = Trip(null, name, city, country, latitude, longitude)

                // Llama al API para guardar el viaje
                val call = RetrofitUtil.getApi()!!.saveTrip(trip)
                call.enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful && response.body() == true) {
                            Toast.makeText(this@TripFormActivity, "¡Viaje guardado con éxito! 🚀", Toast.LENGTH_SHORT).show()
                            finish()  // Cierra la actividad y vuelve a la anterior
                        } else {
                            Toast.makeText(this@TripFormActivity, "No se pudo guardar el viaje. Intenta de nuevo. 😓", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Toast.makeText(this@TripFormActivity, "¡Error de conexión! Revisa tu internet 🌐", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    /**
     * Hace vibrar el dispositivo de forma corta para advertencias o errores menores.
     */
    private fun vibrateShort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(150)
        }
    }

    /**
     * Hace vibrar el dispositivo de forma más larga para confirmar acciones importantes.
     */
    private fun vibrateStrong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(300)
        }
    }

    /**
     * Callback que se ejecuta cuando el mapa está listo para usarse.
     * Configura el mapa, marcador y su comportamiento de arrastre.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Establece el tipo de mapa híbrido (satélite + calles)
        map!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        // Inicializa el marcador en coordenadas (0,0) y permite que sea arrastrable
        val latLng = LatLng(0.0, 0.0)
        map?.addMarker(MarkerOptions().position(latLng).draggable(true))

        // Centra la cámara en la posición inicial con un zoom adecuado
        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.zoomTo(8f))

        // Permite que el usuario arrastre el marcador y actualiza las coordenadas
        map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {
                // No se necesita manejar este evento
            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Actualiza las coordenadas cuando el marcador se suelta
                latitude = marker.position.latitude
                longitude = marker.position.longitude
            }

            override fun onMarkerDragStart(p0: Marker) {
                // No se necesita manejar este evento
            }
        })
    }
}
