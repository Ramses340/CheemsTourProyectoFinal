package mx.itson.cheemstour.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import mx.itson.cheemstour.R
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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.elem_trip, parent, false)

        val trip = trips[position]



        val txtName = view.findViewById<TextView>(R.id.name)
        val txtCity = view.findViewById<TextView>(R.id.city)
        val txtCountry = view.findViewById<TextView>(R.id.country)

        txtName.text = trip.name
        txtCity.text = trip.city
        txtCountry.text = trip.country

        return view
    }

}