package com.example.helloworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditPlacesActivity : AppCompatActivity() {
    private val TAG = "btaFourthActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_places)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val timestamp = intent.getStringExtra("timestamp")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        val altitude = intent.getStringExtra("altitude")

        Log.d(TAG, "Timestamp: $timestamp, Latitude: $latitude, Longitude: $longitude, Altitude: $altitude")

        val previousButton: Button = findViewById(R.id.previousButton)
        previousButton.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
            finish()
        }


    }
}