package mx.itson.cheemstour.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import android.widget.Button
import mx.itson.cheemstour.EditTripActivity
import mx.itson.cheemstour.R
import mx.itson.cheemstour.TripListActivity
import mx.itson.cheemstour.entities.Trip

class TripAdapter(
    context: Context,
    trips : List<Trip>
) : BaseAdapter(){

    var context : Context = context
    var trips : List<Trip> = trips

    override fun getCount(): Int {
        return trips.size
    }

    override fun getItem(position: Int): Any {
        return trips[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var elemento = LayoutInflater.from(context).inflate(R.layout.elem_trip, null)
        try {
            val trip = getItem(position) as Trip

            val txtName: TextView = elemento.findViewById(R.id.name)
            txtName.text = trip.name

            val txtCity: TextView = elemento.findViewById(R.id.city)
            txtCity.text = trip.city

            val txtCountry: TextView = elemento.findViewById(R.id.country)
            txtCountry.text = trip.country

            val btnEdit: Button = elemento.findViewById(R.id.btnEdit)
            val btnDelete: Button = elemento.findViewById(R.id.btnDelete)


            btnDelete.setOnClickListener {
                trip.id?.let { it1 -> (context as TripListActivity).deleteTrip(it1) }
            }

            btnEdit.setOnClickListener {
                (context as TripListActivity).editTrip(trip)
            }

            val intent = Intent(context, EditTripActivity::class.java)
            intent.putExtra("trip", trip)
            context.startActivity(intent)

        } catch(ex: Exception) {
            Log.e("Error showing trips", ex.message.toString())
        }
        return elemento
    }

}