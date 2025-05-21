package mx.itson.cheemstour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que permite editar un viaje existente.
 * Muestra los datos actuales del viaje para que el usuario pueda modificarlos,
 * y luego actualiza la información en el servidor mediante una llamada API.
 */
class EditTripActivity : AppCompatActivity() {

    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        // Obtener el objeto Trip enviado desde la actividad anterior
        trip = intent.getSerializableExtra("trip") as? Trip

        // Si no se recibe ningún viaje, cerrar esta actividad
        if (trip == null) {
            finish()
            return
        }

        // Referencias a los campos de entrada y botón en el layout
        val editName = findViewById<EditText>(R.id.editName)
        val editCity = findViewById<EditText>(R.id.editCity)
        val editCountry = findViewById<EditText>(R.id.editCountry)
        val editLatitude = findViewById<EditText>(R.id.editLatitude)
        val editLongitude = findViewById<EditText>(R.id.editLongitude)
        val btnSave = findViewById<Button>(R.id.btnSaveChanges)

        // Rellenar los campos con los datos actuales del viaje
        editName.setText(trip!!.name)
        editCity.setText(trip!!.city)
        editCountry.setText(trip!!.country)
        editLatitude.setText(trip!!.latitude.toString())
        editLongitude.setText(trip!!.longitude.toString())

        // Configurar el botón para guardar cambios
        btnSave.setOnClickListener {
            // Crear un nuevo objeto Trip con los datos actualizados
            val updatedTrip = Trip(
                id = trip!!.id,
                name = editName.text.toString(),
                city = editCity.text.toString(),
                country = editCountry.text.toString(),
                latitude = editLatitude.text.toString().toDouble(),
                longitude = editLongitude.text.toString().toDouble()
            )
            // Enviar la actualización al servidor
            updateTrip(updatedTrip)
        }
    }

    /**
     * Envía una solicitud para actualizar el viaje en el servidor mediante Retrofit.
     * Muestra un mensaje de éxito o error según la respuesta.
     *
     *
     */
    private fun updateTrip(updatedTrip: Trip) {
        val call = RetrofitUtil.getApi()!!.updateTrip(updatedTrip.id, updatedTrip)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    // Mostrar mensaje de éxito y volver a la lista de viajes
                    Toast.makeText(this@EditTripActivity, getString(R.string.text_updated_successful), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditTripActivity, TripListActivity::class.java))
                    finish()
                } else {
                    // Mostrar mensaje de error si la actualización falló
                    Toast.makeText(this@EditTripActivity, getString(R.string.text_updated_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                // Registrar el error en Logcat y mostrar mensaje de error
                Log.e("EditTripActivity", "Error actualizando viaje: ${t.message}")
                Toast.makeText(this@EditTripActivity, getString(R.string.text_updated_error), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
