package com.example.helloworld

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.helloworld.room.AppDatabase
import com.example.helloworld.room.PlacesEntity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LocationListener, NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var locationSwitch: Switch
    var latestLocation: Location? = null

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: The activity is being created.");

        val userIdentifier = getUserIdentifier()
        if(userIdentifier == null) {
            showUserIdentifier()
        } else {
            val textView: TextView = findViewById(R.id.mainTextView)
            textView.text = "Hello $userIdentifier!"
            Toast.makeText(this,"User ID: $userIdentifier",Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        val textView: TextView = findViewById(R.id.mainTextView)
        val locationText = getString(R.string.location_text, location.latitude, location.longitude)
        textView.text = locationText
        saveCoordinatesToFile(location.latitude, location.longitude, location.altitude)

        val toastText = "New location: ${location.latitude}, ${location.longitude}, ${location.altitude}"
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()

        //If the database doesn't have data, it will create 3 default places
        val db = AppDatabase.getDatabase(this)
        var countItems = 0

        lifecycleScope.launch {
            //countItems = db.placesDao().getCount()
        }

        if (countItems == 0) {
            savePlaceToDatabase("Campus Sur UPM", "School", "Campus where computer science is studied", 40.3897, -3.6278, 650.0, System.currentTimeMillis())
            savePlaceToDatabase("Puerta del Sol", "City", "a square in the Spanish city of Madrid", 40.4167, -3.7033, 650.0, System.currentTimeMillis())
            savePlaceToDatabase("Retiro", "Park", "A beautiful park with high trees", 40.4153, -3.6839, 650.0, System.currentTimeMillis())
        }

        Log.d(TAG, "countItems in database: ${countItems}.")
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
        val sharedPreferences=this.getSharedPreferences("AppPreferences",android.content.Context.MODE_PRIVATE)
        sharedPreferences.edit().apply(){
            putString("userIdentifier",userIdentifier)
            apply()
        }
    }
    private fun getUserIdentifier(): String?{
        val sharedPreferences=this.getSharedPreferences("AppPreferences",android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getString("userIdentifier",null)
    }

    private fun saveCoordinatesToFile(latitude: Double, longitude: Double, altitude: Double){
        val fileName="gps_coordinates.csv"
        val file=java.io.File(filesDir,fileName)
        val timestamp=System.currentTimeMillis()
        val latStr = String.format(java.util.Locale.US, "%.4f",latitude)
        val lonStr = String.format(java.util.Locale.US,"%.4f",longitude)
        val altStr = String.format(java.util.Locale.US,"%.4f",altitude)

        file.appendText("$timestamp, $latStr, $lonStr, $altStr\n")
    }

    //PLACES
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                // Handle home action
            }
            R.id.second_activity -> {
                val intent = Intent(this, PlacesActivity::class.java)
                startActivity(intent)
            }
            R.id.third_activity -> {
                val intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
            }
            R.id.open_street_map -> {
                val intent = Intent(this, OpenStreetMapsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("location", latestLocation)
                intent.putExtra("locationBundle", bundle)
                startActivity(intent)
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}