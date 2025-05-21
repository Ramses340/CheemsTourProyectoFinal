package mx.itson.cheemstour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mx.itson.cheemstour.entities.Trip
import mx.itson.cheemstour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTripActivity : AppCompatActivity() {

    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        trip = intent.getSerializableExtra("trip") as? Trip

        if (trip == null) {
            finish()
            return
        }

        val editName = findViewById<EditText>(R.id.editName)
        val editCity = findViewById<EditText>(R.id.editCity)
        val editCountry = findViewById<EditText>(R.id.editCountry)
        val editLatitude = findViewById<EditText>(R.id.editLatitude)
        val editLongitude = findViewById<EditText>(R.id.editLongitude)
        val btnSave = findViewById<Button>(R.id.btnSaveChanges)

        // Rellenar campos
        editName.setText(trip!!.name)
        editCity.setText(trip!!.city)
        editCountry.setText(trip!!.country)
        editLatitude.setText(trip!!.latitude.toString())
        editLongitude.setText(trip!!.longitude.toString())

        btnSave.setOnClickListener {
            val updatedTrip = Trip(
                id = trip!!.id,
                name = editName.text.toString(),
                city = editCity.text.toString(),
                country = editCountry.text.toString(),
                latitude = editLatitude.text.toString().toDouble(),
                longitude = editLongitude.text.toString().toDouble()
            )

            updateTrip(updatedTrip)
        }
    }

    private fun updateTrip(updatedTrip: Trip) {
        val call = RetrofitUtil.getApi()!!.updateTrip(updatedTrip.id, updatedTrip)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    Toast.makeText(this@EditTripActivity, getString(R.string.text_updated_successful), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditTripActivity, TripListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@EditTripActivity, getString(R.string.text_updated_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("EditTripActivity", "Error actualizando viaje: ${t.message}")
            }
        })
    }
}