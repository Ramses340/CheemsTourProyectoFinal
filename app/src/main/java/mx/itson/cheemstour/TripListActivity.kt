package mx.itson.cheemstour

import android.os.Bundle
import android.util.Log
import android.widget.ListView
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


}