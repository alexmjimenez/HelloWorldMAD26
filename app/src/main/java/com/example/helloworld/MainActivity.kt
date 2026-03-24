package com.example.helloworld

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.helloworld.room.AppDatabase
import com.example.helloworld.room.PlacesEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var locationSwitch: Switch
    var latestLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        val userIdentifier = getUserIdentifier()
        val tvWelcome: TextView = findViewById(R.id.tvWelcome)
        if(userIdentifier == null) {
            showUserIdentifier()
        } else {
            tvWelcome.text = "Hello $userIdentifier!"
        }
        val btnSettings: ImageView = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.places_activity -> {
                    val intent = Intent(this, PlacesActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("location", latestLocation)
                    intent.putExtra("locationBundle", bundle)
                    startActivity(intent)
                }
                R.id.historial_activity -> startActivity(Intent(this, HistorialActivity::class.java))
                R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.open_street_map -> {
                    val intent = Intent(this, OpenStreetMapsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("location", latestLocation)
                    intent.putExtra("locationBundle", bundle)
                    startActivity(intent)
                }
            }
            true
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationSwitch = findViewById(R.id.locationSwitch)
        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                locationSwitch.text = "Disable location"
                startLocationUpdates()
            } else {
                locationSwitch.text = "Enable location"
                stopLocationUpdates()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val userIdentifier = getUserIdentifier()
        val tvWelcome: TextView = findViewById(R.id.tvWelcome)
        if (userIdentifier != null) {
            tvWelcome.text = "Hello $userIdentifier!"
        }
        val lat: Double
        val lon: Double
        if (latestLocation != null) {
            lat = latestLocation!!.latitude
            lon = latestLocation!!.longitude
            Log.d(TAG, "onResume: Reading last coordinates -> $lat, $lon")
        } else {
            lat = 40.3898
            lon = -3.6278
            Log.d(TAG, "onResume: Coordinates not read yet. Using default coordinates -> $lat, $lon")
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionCode
            )
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    private fun stopLocationUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        latestLocation = location
        saveCoordinatesToFile(location.latitude, location.longitude, location.altitude)
        val toastText = "New location: ${location.latitude}, ${location.longitude}, ${location.altitude}"
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun showUserIdentifier(){
        val builder= android.app.AlertDialog.Builder(this)
        builder.setTitle("Enter user")
        val input= android.widget.EditText(this)
        val userIdentifier=getUserIdentifier()
        if(userIdentifier!=null){
            input.setText(userIdentifier)
        }
        builder.setView(input)
        builder.setPositiveButton("OK"){ dialog, which ->
            val userInput=input.text.toString()
            if(userInput.isNotBlank()){
                saveUserIdentifier(userInput)
                val tvWelcome: TextView = findViewById(R.id.tvWelcome)
                tvWelcome.text = "Hello $userInput!"
                Toast.makeText(this,"User ID saved: $userInput", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"User ID cannot be blank", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("Cancel"){ dialog, which ->
            dialog.cancel()
            Toast.makeText(this,"Bye!!!", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    private fun saveUserIdentifier(userIdentifier: String){
        val sharedPreferences=this.getSharedPreferences("AppPreferences",MODE_PRIVATE)
        sharedPreferences.edit().apply(){
            putString("userIdentifier",userIdentifier)
            apply()
        }
    }
    private fun getUserIdentifier(): String?{
        val sharedPreferences=this.getSharedPreferences("AppPreferences",MODE_PRIVATE)
        return sharedPreferences.getString("userIdentifier",null)
    }

    private fun saveCoordinatesToFile(latitude: Double, longitude: Double, altitude: Double){
        try {
            val fileName = "gps_coordinates.csv"
            val file = java.io.File(filesDir, fileName)
            val timestamp = System.currentTimeMillis()
            val latStr = String.format(java.util.Locale.US, "%.4f", latitude)
            val lonStr = String.format(java.util.Locale.US, "%.4f", longitude)
            val altStr = String.format(java.util.Locale.US, "%.4f", altitude)
            file.appendText("$timestamp, $latStr, $lonStr, $altStr\n")
        } catch (e: Exception) {
            Log.e(TAG, "Error guardando el archivo CSV: ${e.message}")
        }
    }

    private fun savePlaceToDatabase(name: String, type: String, description: String, latitude: Double, longitude: Double, altitude: Double, timestamp: Long) {
        val places = PlacesEntity(
            name = name,
            type = type,
            description = description,
            timestamp = timestamp,
            latitude = latitude,
            longitude = longitude,
            altitude = altitude
        )
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            db.placesDao().insert(places)
        }
    }
}