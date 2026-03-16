package com.example.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditPlacesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_places)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.previousButton)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val placeName = intent.getStringExtra("name") ?: ""
        val latitude = intent.getStringExtra("latitude") ?: "0.0"
        val longitude = intent.getStringExtra("longitude") ?: "0.0"
        val etPlaceName = findViewById<EditText>(R.id.etPlaceName)
        val tvPlaceCoords = findViewById<TextView>(R.id.tvPlaceCoords)
        val btnSave = findViewById<Button>(R.id.btnSavePlace)
        val btnDelete = findViewById<Button>(R.id.btnDeletePlace)
        val btnBack = findViewById<Button>(R.id.previousButton)
        etPlaceName.setText(placeName)
        tvPlaceCoords.text = "Latitude: $latitude\nLongitude: $longitude"

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val newName = etPlaceName.text.toString()
            Toast.makeText(this, "Saved: $newName", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnDelete.setOnClickListener {
            Toast.makeText(this, "Deleted: $placeName", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}