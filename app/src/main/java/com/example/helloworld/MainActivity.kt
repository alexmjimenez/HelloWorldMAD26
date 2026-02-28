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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.intellij.lang.annotations.Identifier

class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    var latestLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val userIdentifier=getUserIdentifier()
        if(userIdentifier==null){
            showUserIdentifier()
        }else{
            android.widget.Toast.makeText(this,"User ID: $userIdentifier",android.widget.Toast.LENGTH_SHORT).show()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: The activity is being created.");

        //Next navigation to second activity
        val buttonNext: Button = findViewById(R.id.mainButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("location", latestLocation)
            intent.putExtra("locationBundle", bundle)
            startActivity(intent)
        }

        //Next navigation to open street map activity
        val buttonOsm: Button = findViewById(R.id.osmButton)
        buttonOsm.setOnClickListener {
            if (latestLocation != null) {
                val intent = Intent(this, OpenStreetMapsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("location", latestLocation)
                intent.putExtra("locationBundle", bundle)
                startActivity(intent)
            }else{
                Log.e(TAG, "Location not set yet.")
            }
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startLocationUpdates()
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
                android.widget.Toast.makeText(this,"User ID saved: $userInput", android.widget.Toast.LENGTH_LONG).show()
            }else{
                android.widget.Toast.makeText(this,"User ID cannot be blank", android.widget.Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("Cancel"){ dialog, which ->
            dialog.cancel()
            android.widget.Toast.makeText(this,"Bye!!!", android.widget.Toast.LENGTH_LONG).show()
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
}