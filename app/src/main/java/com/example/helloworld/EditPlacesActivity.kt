package com.example.helloworld

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.helloworld.room.AppDatabase
import com.example.helloworld.room.PlacesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditPlacesActivity : AppCompatActivity() {

    private val TAG = "btaEditPlacesActivity"
    private lateinit var etPlaceName: EditText
    private lateinit var etType: EditText
    private lateinit var etDescription: EditText
    private lateinit var etLatitude: EditText
    private lateinit var etLongitude: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_places)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        val placeName = intent.getStringExtra("name") ?: ""
        val type = intent.getStringExtra("type") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        var latitude = intent.getStringExtra("latitude") ?: "0.0"
        var longitude = intent.getStringExtra("longitude") ?: "0.0"

        //Solo para los que haya recibido por el botón de "Add new place"
        if (location != null) {
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
        }

        //Obtener referencias de EditText
        etPlaceName = findViewById(R.id.etPlaceName)
        etType = findViewById(R.id.etType)
        etDescription = findViewById(R.id.etDescription)
        etLatitude = findViewById(R.id.etLatitude)
        etLongitude = findViewById(R.id.etLongitude)

        Log.d(TAG, "Latitude: $latitude, Longitude: $longitude")

        val btnSave = findViewById<Button>(R.id.btnSavePlace)
        val btnDelete = findViewById<Button>(R.id.btnDeletePlace)
        val btnBack = findViewById<Button>(R.id.previousButton)

        etPlaceName.setText(placeName)
        etType.setText(type)
        etDescription.setText(description)
        etLatitude.setText(latitude)
        etLongitude.setText(longitude)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            if (etPlaceName.text.toString().isNotEmpty()){
                showAddConfirmationDialog()
            }
        }

        btnDelete.setOnClickListener {
            if (placeName.isNotEmpty()) {
                showDeleteConfirmationDialog(placeName)
            }
        }
    }

    private fun showDeleteConfirmationDialog(name: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this place?\n\n" +
                    "📍 Name: $name\n" +
                    "📍 Latitude: ${etLatitude.text}\n" +
                    "📍 Longitude: ${etLongitude.text}\n")
            .setPositiveButton("Delete") { _, _ ->
                deletePlace(name)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePlace(name: String) {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch(Dispatchers.IO) {
            db.placesDao().deleteWithName(name)
            Log.d(TAG, "Coordinate with name $name deleted.")

            // Volver a la actividad anterior después de borrar en el hilo principal
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@EditPlacesActivity, PlacesActivity::class.java))
                finish()
            }
        }
    }

    private fun showUpdateConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Update")
            .setMessage("Are you sure you want to update this place?\n\n" +
                    "📍 Name: ${etPlaceName.text}\n" +
                    "📍 Type: ${etType.text}\n" +
                    "📍 Latitude: ${etLatitude.text}\n" +
                    "📍 Longitude: ${etLongitude.text}\n")
            .setPositiveButton("Update") { _, _ ->
                updatePlace()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updatePlace() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val name = etPlaceName.text.toString()
            val existingPlace = db.placesDao().getPlaceByName(name)

            if (existingPlace != null) {
                val updatedPlace = PlacesEntity(
                    name = name,
                    type = etType.text.toString(),
                    description = etDescription.text.toString(),
                    timestamp = existingPlace.timestamp,
                    latitude = etLatitude.text.toString().toDouble(),
                    longitude = etLongitude.text.toString().toDouble(),
                    altitude = existingPlace.altitude
                )

                db.placesDao().updatePlace(updatedPlace)
                Log.d(TAG, "✅ Place updated: $updatedPlace")
            } else {
                Log.e(TAG, "⚠️ No place found with name $name")
            }

            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    private fun showAddConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Save")
            .setMessage("Are you sure you want to save this place?\n\n" +
                    "📍 Name: ${etPlaceName.text}\n" +
                    "📍 Type: ${etType.text}\n" +
                    "📍 Latitude: ${etLatitude.text}\n" +
                    "📍 Longitude: ${etLongitude.text}\n")
            .setPositiveButton("Add") { _, _ ->
                addPlace()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addPlace() {
        val db = AppDatabase.getDatabase(this)
        val firebaseDb=com.google.firebase.database.FirebaseDatabase.getInstance("https://helloworld-40476-default-rtdb.firebaseio.com/").reference

        lifecycleScope.launch(Dispatchers.IO) {
            val timestamp=System.currentTimeMillis()
            val newPlace = PlacesEntity(
                name = etPlaceName.text.toString(),
                type = etType.text.toString(),
                description = etDescription.text.toString(),
                timestamp = timestamp,
                latitude = etLatitude.text.toString().toDouble(),
                longitude = etLongitude.text.toString().toDouble(),
                altitude = (500.0).toString().toDouble()
            )

            db.placesDao().insert(newPlace)
            Log.d(TAG, "Place added: $newPlace")
            firebaseDb.child("places").child(newPlace.name).setValue(newPlace).addOnSuccessListener {
                Log.d(TAG, "Place uploaded to Firebase")
            }.addOnFailureListener { e->
                Log.d(TAG, "Error uploading to Firebase: ${e.message}")
            }

            withContext(Dispatchers.Main) {
                startActivity(Intent(this@EditPlacesActivity, PlacesActivity::class.java))
                finish()
            }
        }
    }
}