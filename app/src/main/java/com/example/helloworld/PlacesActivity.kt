package com.example.helloworld

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.helloworld.room.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlacesActivity : AppCompatActivity() {
    private val TAG = "btaPlacesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val listView: ListView = findViewById(R.id.lvPlace)

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val dbPlaces = db.placesDao().getAll()

            val roomPlaces = dbPlaces.map {
                listOf(it.timestamp.toString(), it.latitude.toString(), it.longitude.toString(), it.altitude.toString())
            }

            Log.d(TAG, "Datos obtenidos de Room: $roomPlaces")

            // Instanciar el adaptador con los datos de Room directamente
            val adapter = PlacesAdapter(this@PlacesActivity, roomPlaces)
            listView.adapter = adapter  // Asignar el adaptador al ListView
        }
    }

    private class PlacesAdapter(context: Context, private val placesList: List<List<String>>) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item_place, placesList) {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item_place, parent, false)

            val placesTextView: TextView = view.findViewById(R.id.tvPlace)

            val item = placesList[position]

            try {
                placesTextView.text = item[0]
            } catch (e: Exception) {
                Log.e("PlacesAdapter", "Error convirtiendo valores: ${e.message}")
            }

            /*view.setOnClickListener {
                val intent = Intent(context, ThirdActivity::class.java).apply {
                    putExtra("timestamp", item[0])
                    putExtra("latitude", item[1])
                    putExtra("longitude", item[2])
                    putExtra("altitude", item[3])
                }
                context.startActivity(intent)
            }*/
            return view
        }
    }
}