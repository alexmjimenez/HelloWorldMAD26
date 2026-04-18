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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.helloworld.room.AppDatabase

data class WeatherResponse(val main: MainData, val weather: List<WeatherData>, val name: String)
data class MainData(val temp:Double)
data class WeatherData(val description:String, val icon: String)

interface WeatherApiServuce {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse
}
class OpenStreetMapsActivity : AppCompatActivity() {
    private val TAG = "btaOpenStreetMapActivity"
    private lateinit var map: MapView

/*    val gymkhanaCoords = listOf(
        GeoPoint(40.38779608214728, -3.627687914352839), // Tennis
        GeoPoint(40.38788595319803, -3.627048250272035), // Futsal outdoors
        GeoPoint(40.3887315224542, -3.628643539758645), // Fashion and design
        GeoPoint(40.38926842612264, -3.630067893975619), // Topos
        GeoPoint(40.38956358584258, -3.629046081389352), // Teleco
        GeoPoint(40.38992125672989, -3.6281366497769714), // ETSISI
        GeoPoint(40.39037466191718, -3.6270256763598447), // Library
        GeoPoint(40.389855884803005, -3.626782180787362) // CITSEM
    )*/

/*    val gymkhanaNames = listOf(
        "Tennis",
        "Futsal outdoors",
        "Fashion and design school",
        "Topography school",
        "Telecommunications school",
        "ETSISI",
        "Library",
        "CITSEM"
    )*/

    val defaultTrashCoords = listOf(
        GeoPoint(40.38779608214728, -3.627687914352839), // Tennis
        GeoPoint(40.38788595319803, -3.627048250272035), // Futsal outdoors
        GeoPoint(40.3887315224542, -3.628643539758645), // Fashion and design
        GeoPoint(40.38926842612264, -3.630067893975619), // Topos
        GeoPoint(40.38956358584258, -3.629046081389352), // Teleco
    )

    val defaultTrashNames = listOf(
        "Organic Waste (Tennis)",
        "Plastics & Packaging (Courts)",
        "Paper (Design School)",
        "General Waste (Topography)",
        "Recycling Point (Teleco)"
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

        addTrashMarkers(map, defaultTrashCoords, defaultTrashNames, this)
        loadUserTrashCansFromDatabase()
        obtenerTiempoAtmosferico(lat, lon)
    }

    private fun loadUserTrashCansFromDatabase() {
        val db=AppDatabase.getDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userPlaces=db.placesDao().getAll()
                withContext(Dispatchers.Main) {
                    for (place in userPlaces) {
                        val marker=Marker(map)
                        marker.position=GeoPoint(place.latitude, place.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        if (place.type.contains("Trash", ignoreCase = true)) {
                            marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_menu_delete) as BitmapDrawable
                            marker.title="${place.name} (Trash Can)"
                        } else {
                            if (place.type.contains("Park", ignoreCase = true)) {
                                marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_menu_gallery) as BitmapDrawable
                            } else if (place.type.contains("Shop", ignoreCase = true)) {
                                marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_popup_reminder) as BitmapDrawable
                            } else if (place.type.contains("Taller", ignoreCase = true)) {
                                marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_menu_preferences) as BitmapDrawable
                            } else if (place.type.contains("Mail", ignoreCase = true)) {
                                marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_dialog_email) as BitmapDrawable
                            } else {
                                marker.icon=ContextCompat.getDrawable(this@OpenStreetMapsActivity, android.R.drawable.ic_dialog_map) as BitmapDrawable
                            }


                            marker.title="${place.name} (Place)"
                        }
                        map.overlays.add(marker)
                    }
                    map.invalidate()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading user points: ${e.message}")
            }
        }
    }
    private fun obtenerTiempoAtmosferico(lat: Double, lon: Double) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val apiKey = sharedPreferences.getString("apiKey", "")
        if (apiKey.isNullOrEmpty()) {
            Log.e(TAG, "API Key is empty. Add it in settings")
            lifecycleScope.launch(Dispatchers.Main) {
                findViewById<TextView>(R.id.tvCityName).text = "API Key is left"
            }
            return
        }

        val retrofit=Retrofit.Builder().baseUrl("https://api.openweathermap.org/").addConverterFactory(
            GsonConverterFactory.create()).build()
        val apiService=retrofit.create(WeatherApiServuce::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response=apiService.getCurrentWeather(lat, lon, apiKey)
                val temp=response.main.temp.toInt()
                val description=response.weather[0].description
                val cityName = response.name
                val iconCode=response.weather[0].icon
                val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"

                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.tvTemperature).text = "$temp °C"
                    findViewById<TextView>(R.id.tvWeatherDesc).text = description.replaceFirstChar { it.uppercase() }
                    findViewById<TextView>(R.id.tvCityName).text = cityName

                    val imageView= findViewById<android.widget.ImageView>(R.id.ivWeatherIcon)
                    com.bumptech.glide.Glide.with(this@OpenStreetMapsActivity)
                        .load(iconUrl)
                        .centerCrop()
                        .override(150, 150)
                        .error(android.R.drawable.ic_dialog_alert)
                        .into(imageView)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error obtaining weather: ${e.message}")
            }
        }
    }

    fun addTrashMarkers(map: MapView, coords: List<GeoPoint>, names: List<String>, context: Context) {
        for (i in coords.indices) {
            val marker=Marker(map)
            marker.position=coords[i]
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon=ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete) as BitmapDrawable
            marker.title=names[i]
            map.overlays.add(marker)
        }
    }

/*    fun addGymkhanaMarkers(map: MapView, coords: List<GeoPoint>, names: List<String>, context: Context) {
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
    }*/
}