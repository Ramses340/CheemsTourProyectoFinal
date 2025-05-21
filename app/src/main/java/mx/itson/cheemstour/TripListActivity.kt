package mx.itson.cheemstour

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mx.itson.cheemstour.adapters.TripAdapter
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que muestra la lista de viajes guardados.
 * Permite editar o eliminar viajes.
 */
class TripListActivity : AppCompatActivity() {

    private var listTrips: ListView? = null
    private var trips: MutableList<Trip> = mutableListOf()
    private var tripAdapter: TripAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_list)

        listTrips = findViewById(R.id.list_trips)
        tripAdapter = TripAdapter(this, trips)
        listTrips?.adapter = tripAdapter

        // Obtiene la lista de viajes desde el servidor
        getTrips()
    }

    /**
     * Elimina un viaje dado su ID.
     * Muestra un Toast con el resultado y regresa a MainActivity si fue exitoso.
     * También genera una vibración para dar retroalimentación al usuario.
     */
    fun deleteTrip(tripId: Int) {
        val context = this
        val call: Call<Boolean> = RetrofitUtil.getApi()!!.deleteTrip(tripId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isDeleted = response.body() ?: false
                    if (isDeleted) {
                        vibrateStrong()
                        Toast.makeText(context, "✅ Viaje eliminado correctamente. ¡Buen trabajo!", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        vibrateShort()
                        Toast.makeText(context, "⚠️ No se pudo eliminar el viaje. Intenta de nuevo.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    vibrateShort()
                    Log.e("Error", "Falló la eliminación: ${response.code()}")
                    Toast.makeText(context, "⚠️ Error al eliminar el viaje. Código: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                vibrateShort()
                Log.e("Error", "Error al llamar API: ${t.message}")
                Toast.makeText(context, "❌ No se pudo conectar con el servidor. Verifica tu red.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Abre la actividad para editar un viaje pasando el objeto trip.
     */
    fun editTrip(trip: Trip) {
        val intent = Intent(this, EditTripActivity::class.java)
        intent.putExtra("trip", trip)
        startActivity(intent)
    }

    /**
     * Solicita la lista de viajes al servidor y actualiza el adaptador.
     */
    private fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi()!!.getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        trips.clear()
                        trips.addAll(it)
                        tripAdapter?.notifyDataSetChanged()
                    }
                } else {
                    Log.e("TripListActivity", "Respuesta fallida: ${response.code()}")
                    Toast.makeText(this@TripListActivity, "🚫 Error al cargar los viajes. Intenta más tarde.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("TripListActivity", "Error en la API: ${t.message}")
                Toast.makeText(this@TripListActivity, "❌ Error de red al cargar los viajes.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Hace vibrar el dispositivo de forma breve para advertencias o errores.
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
     * Hace vibrar el dispositivo de forma más intensa para confirmaciones exitosas.
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
}
