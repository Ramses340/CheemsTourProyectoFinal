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
    trips: List<Trip>
) : BaseAdapter() {

    // Contexto de la aplicación, necesario para inflar vistas e iniciar actividades
    var context: Context = context

    // Lista de viajes para mostrar en el adapter
    var trips: List<Trip> = trips

    // Retorna el número de elementos (viajes) en la lista
    override fun getCount(): Int {
        return trips.size
    }

    // Obtiene el elemento Trip en la posición indicada
    override fun getItem(position: Int): Any {
        return trips[position]
    }

    // Retorna el ID del ítem, aquí usamos la posición como ID
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Crea y devuelve la vista para un ítem en la lista
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflamos la vista para el elemento de la lista
        val elemento = LayoutInflater.from(context).inflate(R.layout.elem_trip, null)

        try {
            val trip = getItem(position) as Trip

            // Asignamos los valores de nombre, ciudad y país a los TextViews
            val txtName: TextView = elemento.findViewById(R.id.name)
            txtName.text = trip.name

            val txtCity: TextView = elemento.findViewById(R.id.city)
            txtCity.text = trip.city

            val txtCountry: TextView = elemento.findViewById(R.id.country)
            txtCountry.text = trip.country

            // Botones para editar y eliminar viaje
            val btnEdit: Button = elemento.findViewById(R.id.btnEdit)
            val btnDelete: Button = elemento.findViewById(R.id.btnDelete)

            // Listener para eliminar viaje, usa función en TripListActivity
            btnDelete.setOnClickListener {
                Log.i("TripAdapter", "Eliminar viaje con id: ${trip.id}")
                trip.id?.let { id ->
                    (context as TripListActivity).deleteTrip(id)
                }
            }

            // Listener para editar viaje: abre EditTripActivity con datos del viaje
            btnEdit.setOnClickListener {
                Log.i("TripAdapter", "Editar viaje: ${trip.name}")
                (context as TripListActivity).editTrip(trip)

                val intent = Intent(context, EditTripActivity::class.java)
                intent.putExtra("trip", trip)
                context.startActivity(intent)
            }

        } catch (ex: Exception) {
            // Log de error con mensaje y stacktrace para  depurar
            Log.e("TripAdapter", "Error mostrando viajes: ${ex.message}", ex)
        }

        return elemento
    }
}
