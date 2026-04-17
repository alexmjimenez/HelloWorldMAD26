package com.example.helloworld

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.views.overlay.Polyline
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class OpenStreetMapsActivity : AppCompatActivity() {
    private val TAG = "btaOpenStreetMapActivity"

    private lateinit var map: MapView

    val gymkhanaCoords = listOf(
        GeoPoint(40.38779608214728, -3.627687914352839), // Tennis
        GeoPoint(40.38788595319803, -3.627048250272035), // Futsal outdoors
        GeoPoint(40.3887315224542, -3.628643539758645), // Fashion and design
        GeoPoint(40.38926842612264, -3.630067893975619), // Topos
        GeoPoint(40.38956358584258, -3.629046081389352), // Teleco
        GeoPoint(40.38992125672989, -3.6281366497769714), // ETSISI
        GeoPoint(40.39037466191718, -3.6270256763598447), // Library
        GeoPoint(40.389855884803005, -3.626782180787362) // CITSEM
    )

    val gymkhanaNames = listOf(
        "Tennis",
        "Futsal outdoors",
        "Fashion and design school",
        "Topography school",
        "Telecommunications school",
        "ETSISI",
        "Library",
        "CITSEM"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting activity...")
        enableEdgeToEdge()
        setContentView(R.layout.activity_open_street_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().load(this,getSharedPreferences("osmdroid", MODE_PRIVATE))
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        val lat = intent.getDoubleExtra("LAT", 40.3897)
        val lon = intent.getDoubleExtra("LON", -3.6278)
        val mapController = map.controller
        mapController.setZoom(18.0)
        val startPoint = GeoPoint(lat,lon)
        mapController.setCenter(startPoint)
        val marker = Marker(map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(this, android.R.drawable.ic_secure) as BitmapDrawable
        marker.title = "Selected location"
        map.overlays.add(marker)

        addGymkhanaMarkers(map, gymkhanaCoords, gymkhanaNames, this)
        addRouteMarkers(map, gymkhanaCoords, gymkhanaNames, this)
        obtenerTiempoAtmosferico(lat, lon)
    }

    private fun obtenerTiempoAtmosferico(lat: Double, lon: Double) {
        val apiKey = "3e6cb458858bbbc6f173401b67ceca53"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric&lang=es"
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = URL(url).readText()
                val jsonObject = JSONObject(response)
                val main = jsonObject.getJSONObject("main")
                val temp = main.getDouble("temp").toInt()
                val weatherArray = jsonObject.getJSONArray("weather")
                val description = weatherArray.getJSONObject(0).getString("description")
                val cityName = jsonObject.getString("name")
                val iconCode= weatherArray.getJSONObject(0).getString("icon")
                val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"

                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.tvTemperature).text = "$temp °C"
                    findViewById<TextView>(R.id.tvWeatherDesc).text = description.replaceFirstChar { it.uppercase() }
                    findViewById<TextView>(R.id.tvCityName).text = cityName

                    val imageView= findViewById<android.widget.ImageView>(R.id.ivWeatherIcon)
                    Log.d(TAG, "Cargar icono: $iconUrl")

                    com.bumptech.glide.Glide.with(this@OpenStreetMapsActivity)
                        .load(iconUrl)
                        .centerCrop()
                        .override(150, 150)
                        .error(android.R.drawable.ic_dialog_alert)
                        .into(imageView)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error obteniendo el tiempo: ${e.message}")
            }
        }
    }

    fun addGymkhanaMarkers(map: MapView, coords: List<GeoPoint>, names: List<String>, context: Context) {
        for (i in coords.indices) {
            val marker = Marker(map)
            marker.position = coords[i]
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_compass) as BitmapDrawable
            marker.title = names[i]
            map.overlays.add(marker)
        }
    }

    fun addRouteMarkers(map: MapView, coords: List<GeoPoint>, names: List<String>, context: Context) {
        val polyline = Polyline()
        polyline.setPoints(coords)
        for (i in coords.indices) {
            val marker = Marker(map)
            marker.position = coords[i]
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_compass) as BitmapDrawable
            marker.title = names[i]
            map.overlays.add(marker)
        }
        map.overlays.add(polyline)
    }
}