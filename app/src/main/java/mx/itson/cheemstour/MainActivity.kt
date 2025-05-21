package mx.itson.cheemstour

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad principal de la aplicación.
 * Muestra tres botones que permiten al usuario:
 * - Crear un nuevo viaje.
 * - Ver la lista de viajes existentes.
 * - Ver los viajes en un mapa.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener referencias a los botones del layout
        val btnNew = findViewById<Button>(R.id.btn_new)
        val btnList = findViewById<Button>(R.id.btn_list)
        val btnMap = findViewById<Button>(R.id.btn_mapa)

        // Configurar el botón para crear un nuevo viaje
        btnNew.setOnClickListener {
            val intent = Intent(this, TripFormActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón para mostrar la lista de viajes
        btnList.setOnClickListener {
            val intent = Intent(this, TripListActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón para mostrar el mapa con los viajes
        btnMap.setOnClickListener {
            val intent = Intent(this, TripMapActivity::class.java)
            startActivity(intent)
        }
    }
}
