package mx.itson.cheemstour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

/**
 * Actividad principal de la aplicación CheemsTour.
 *
 * Muestra tres botones al usuario que permiten:
 * - Crear un nuevo viaje.
 * - Ver la lista de viajes existentes.
 * - Ver los viajes en un mapa.
 *
 * También contiene un carrusel de imágenes con destinos destacados.
 *
 * Cada acción proporciona retroalimentación al usuario mediante Toasts interactivos
 * y una breve vibración del dispositivo.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Método llamado al iniciar la actividad.
     * Se encarga de inicializar la interfaz, configurar las acciones de los botones
     * y cargar el carrusel de imágenes de destinos.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a los botones del layout
        val btnNew = findViewById<Button>(R.id.btn_new)
        val btnList = findViewById<Button>(R.id.btn_list)
        val btnMap = findViewById<Button>(R.id.btn_mapa)

        // Obtener referencia al ViewPager2 del carrusel
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // Lista de imágenes para el carrusel (ubicadas en res/drawable)
        val images = listOf(
            R.drawable.paris,
            R.drawable.newyork,
            R.drawable.china,
            R.drawable.cdmx,
            R.drawable.italia,
            R.drawable.brasil,
            R.drawable.japon,
            R.drawable.machu_picchu

        )

        // Asignar el adaptador personalizado al ViewPager2
        viewPager.adapter = CarouselAdapter(images)

        // Configurar el botón para crear un nuevo viaje
        btnNew.setOnClickListener {
            vibrate() // Vibración breve para dar retroalimentación
            Toast.makeText(this, "¡Vamos a crear una nueva aventura! ✈️", Toast.LENGTH_SHORT).show()

            // Iniciar la actividad para crear un nuevo viaje
            val intent = Intent(this, TripFormActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón para mostrar la lista de viajes
        btnList.setOnClickListener {
            vibrate()
            Toast.makeText(this, "Aquí están tus recuerdos de viaje 🗂️", Toast.LENGTH_SHORT).show()

            // Iniciar la actividad que muestra la lista de viajes
            val intent = Intent(this, TripListActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón para mostrar el mapa con los viajes
        btnMap.setOnClickListener {
            vibrate()
            Toast.makeText(this, "Explora tus destinos en el mapa 🗺️", Toast.LENGTH_SHORT).show()

            // Iniciar la actividad que muestra el mapa con los viajes
            val intent = Intent(this, TripMapActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Hace vibrar el dispositivo durante 100 milisegundos, si el hardware lo permite.
     * Esto proporciona retroalimentación háptica al usuario.
     */
    private fun vibrate() {
        // Obtener el servicio de vibración del sistema
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Verificar si el dispositivo tiene hardware de vibración
        if (vibrator.hasVibrator()) {
            // Crear un efecto de vibración de 100 ms con la amplitud predeterminada
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }
}

