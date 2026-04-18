package com.example.helloworld

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.helloworld.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesActivity : AppCompatActivity() {
    private val TAG = "btaPlacesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_places)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editButton: Button=findViewById(R.id.editButton)
        val btnAddTrash: Button=findViewById(R.id.btnAddTrash)
        val bundle=intent.getBundleExtra("locationBundle")
        val location: Location?=bundle?.getParcelable("location")

        editButton.setOnClickListener {
            val intent=Intent(this, EditPlacesActivity::class.java)
            if (location!=null) {
                val locBundle=Bundle()
                locBundle.putParcelable("location", location)
                intent.putExtra("locationBundle", locBundle)
            }
            startActivity(intent)
        }
        btnAddTrash.setOnClickListener {
            val intent=Intent(this, EditPlacesActivity::class.java).apply {
                putExtra("isTrashCan", true)
            }
            if(location!=null) {
                val locBundle=Bundle()
                locBundle.putParcelable("location", location)
                intent.putExtra("locationBundle", locBundle)
            }
            startActivity(intent)
        }

        val listView: ListView = findViewById(R.id.lvPlace)
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dbPlaces = db.placesDao().getAll()
                val roomPlaces = dbPlaces.map {
                    listOf(
                        it.name,
                        it.type,
                        it.latitude.toString(),
                        it.longitude.toString(),
                        it.altitude.toString(),
                        it.timestamp.toString()
                    )
                }
                withContext(Dispatchers.Main) {
                    val adapter = PlacesAdapter(this@PlacesActivity, roomPlaces)
                    listView.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }

    private class PlacesAdapter(context: Context, private val placesList: List<List<String>>) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item_place, placesList) {
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item_place, parent, false)
            val placesTextView: TextView = view.findViewById(R.id.tvPlace)
            val item = placesList[position]

            placesTextView.text = item[0]
            view.setOnClickListener {
                val intent = Intent(context, OpenStreetMapsActivity::class.java).apply {
                    putExtra("LAT", item[2].toDoubleOrNull() ?: 0.0)
                    putExtra("LON", item[3].toDoubleOrNull() ?: 0.0)
                    putExtra("NAME", item[0])
                }
                context.startActivity(intent)
            }

            view.setOnLongClickListener {
                val intent = Intent(context, EditPlacesActivity::class.java).apply {
                    putExtra("name", item[0])
                    putExtra("type", item[1])
                    putExtra("latitude", item[2])
                    putExtra("longitude", item[3])
                }
                context.startActivity(intent)
                true
            }
            return view
        }
    }
}