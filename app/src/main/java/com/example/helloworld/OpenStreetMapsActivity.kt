package com.example.helloworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class OpenStreetMapsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this,getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_open_street_maps)

        val map= findViewById<MapView>(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val lat = intent.getDoubleExtra("LAT", 40.4167)
        val lon = intent.getDoubleExtra("LON", -3.7033)
        val mapController = map.controller
        mapController.setZoom(18.0)
        val startPoint = GeoPoint(lat,lon)
        mapController.setCenter(startPoint)
    }
}