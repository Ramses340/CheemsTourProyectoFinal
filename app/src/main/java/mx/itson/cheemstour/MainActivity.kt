package mx.itson.cheemstour

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNew = findViewById<Button>(R.id.btn_new)
        val btnList = findViewById<Button>(R.id.btn_list)
        val  btnMap = findViewById<Button>(R.id.btn_mapa)

        btnNew.setOnClickListener {
            val intent = Intent(this, TripFormActivity::class.java)
            startActivity(intent)
        }

        btnList.setOnClickListener {
            val intent = Intent(this, TripListActivity::class.java)
            startActivity(intent)
        }

        btnMap.setOnClickListener {
            val intent = Intent(this, TripMapActivity::class.java)
            startActivity(intent)
        }
    }
}
