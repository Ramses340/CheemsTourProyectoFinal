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

class TripFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    var map : GoogleMap? = null
    var latitude : Double =  0.0
    var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_form)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btn_save).setOnClickListener(this)

        var mapaFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapaFragment.getMapAsync(this)
    }




    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_save) {
            val name = findViewById<EditText>(R.id.txt_name).text.toString().trim()
            val city = findViewById<EditText>(R.id.txt_city).text.toString().trim()
            val country = findViewById<EditText>(R.id.txt_country).text.toString().trim()


            if (name.isEmpty() || city.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, R.string.text_complete, Toast.LENGTH_SHORT).show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorAdmin = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorAdmin.defaultVibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(300)
                }

                val trip = Trip(null, name, city, country, latitude, longitude)
                val call = RetrofitUtil.getApi()!!.saveTrip(trip)
                call.enqueue(object : Callback<Boolean> {

                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful && response.body() == true) {
                            Toast.makeText(this@TripFormActivity, R.string.text_trip_saved, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@TripFormActivity, R.string.text_not_saved, Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Toast.makeText(this@TripFormActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onMapReady(googleMap : GoogleMap) {
        map = googleMap
        map!!.mapType = GoogleMap.MAP_TYPE_HYBRID

        var latLng = LatLng(0.0,0.0)
        map?.addMarker(MarkerOptions().position(latLng).draggable(true))

        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.zoomTo(8f))

        map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {
                latitude = marker.position.latitude
                longitude = marker.position.longitude
            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })

    }
}
