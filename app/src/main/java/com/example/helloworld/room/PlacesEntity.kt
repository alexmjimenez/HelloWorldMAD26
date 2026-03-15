package com.example.helloworld.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlacesEntity(
    @PrimaryKey val name: String,
    val type: String,
    val description: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double
)
