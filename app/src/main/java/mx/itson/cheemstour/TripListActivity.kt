package mx.itson.cheemstour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mx.itson.cheemstour.adapters.TripAdapter
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.entities.Weather
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        getTrips()
    }
    fun deleteTrip(tripId: Int) {
        val context = this
        val call: Call<Boolean> = RetrofitUtil.getApi()!!.deleteTrip(tripId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isDeleted = response.body() ?: false
                    if (isDeleted) {
                        Toast.makeText(context, getString(R.string.text_deleted_successful), Toast.LENGTH_LONG).show()
                        // Redirigir o refrescar la lista
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, getString(R.string.text_deleted_error), Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("Error", "Failed to delete trip: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", "Error calling API: ${t.message}")
            }
        })
    }

    fun editTrip(trip: Trip) {
        val intent = Intent(this, EditTripActivity::class.java)
        intent.putExtra("trip", trip)
        startActivity(intent)
    }

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
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("TripListActivity", "Error en la API: ${t.message}")
            }
        })
    }

    fun deleteTrip(tripId: Int) {
        val context = this
        val call: Call<Boolean> = RetrofitUtil.getApi()!!.deleteTrip(tripId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val isDeleted = response.body() ?: false
                    if (isDeleted) {
                        Toast.makeText(context, getString(R.string.text_deleted_successful), Toast.LENGTH_LONG).show()
                        // Redirigir o refrescar la lista
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, getString(R.string.text_deleted_error), Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("Error", "Failed to delete trip: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", "Error calling API: ${t.message}")
            }
        })
    }

    fun editTrip(trip: Trip) {
        val intent = Intent(this, EditTripActivity::class.java)
        intent.putExtra("trip", trip)
        startActivity(intent)
    }


}