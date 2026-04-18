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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val editButton: Button = findViewById(R.id.EditButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditPlacesActivity::class.java)
            startActivity(intent)
        }

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable<Location>("location")

        if (location != null) {
            Log.i(TAG, "onCreate: Location[${location.altitude}][${location.latitude}][${location.longitude}]")
        }

        //Display the file contents
        val listView: ListView = findViewById(R.id.lvFileContents)
        val headerView = layoutInflater.inflate(R.layout.listview_header, null)
        listView.addHeaderView(headerView, null, false)

        val mutCoords = mutableListOf<List<String>>()
        //Read the file contents
        if (readFileLines().isEmpty()) {
            return
        }

        for (line in readFileLines()) {
            mutCoords.add(line.split(",").map {
                it.trim()
            })
        }

        val coords: List<List<String>> = mutCoords

        Log.d(TAG, "Datos obtenidos de Csv: $coords")
        val adapter = CoordinatesAdapter(this@HistorialActivity, coords)
        listView.adapter = adapter
    }

    fun readFileLines(): List<String> {
        val fileName = "gps_coordinates.csv"
        return try {
            //Open the file from internal storage
            openFileInput(fileName).bufferedReader().readLines()
        } catch (e: IOException) {
            listOf("Error reading file: ${e.message}")
        }
    }

    private class CoordinatesAdapter(context: Context, private val coordinatesList: List<List<String>>) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item, coordinatesList) {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item, parent, false)

            val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
            val latitudeTextView: TextView = view.findViewById(R.id.tvLatitude)
            val longitudeTextView: TextView = view.findViewById(R.id.tvLongitude)
            val altitudeTextView: TextView = view.findViewById(R.id.tvAltitude)

            val item = coordinatesList[position]
            timestampTextView.text = formatTimestamp(item[0].toLong())
            latitudeTextView.text = formatCoordinate(item[1].toDouble())
            longitudeTextView.text = formatCoordinate(item[2].toDouble())
            altitudeTextView.text = formatCoordinate(item[3].toDouble())

            view.setOnClickListener {
                val intent = Intent(context, EditPlacesActivity::class.java).apply {
                    putExtra("timestamp", item[0])
                    putExtra("latitude", item[1])
                    putExtra("longitude", item[2])
                    putExtra("altitude", item[3])
                }
                context.startActivity(intent)
            }

            return view
        }

        private fun formatTimestamp(timestamp: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        private fun formatCoordinate(value: Double): String {
            return String.format("%.4f", value)
        }
    }
}