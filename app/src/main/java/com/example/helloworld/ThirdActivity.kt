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

class ThirdActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val buttonNext: Button = findViewById(R.id.mapButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonToThird: Button = findViewById(R.id.homeButton)
        buttonToThird.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable<Location>("location")

        if (location != null) {
            Log.i(TAG, "onCreate: Location[${location.altitude}][${location.latitude}][${location.longitude}]")
        }

        //Display the file contents
        val listView: ListView = findViewById(R.id.lvFileContents)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, readFileLines())
        listView.adapter = adapter
        val headerView = layoutInflater.inflate(R.layout.listview_header, null)
        listView.addHeaderView(headerView, null, false)
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