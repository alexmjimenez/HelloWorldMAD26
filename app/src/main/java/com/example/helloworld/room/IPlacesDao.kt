package com.example.helloworld.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IPlacesDao {
    @Insert
    suspend fun insert(coordinates: PlacesEntity)

    @Query("SELECT * FROM places")
    suspend fun getAll(): List<PlacesEntity>

    @Query("SELECT COUNT(*) FROM places")
    fun getCount(): Int

    @Query("DELETE FROM places WHERE name = :name")
    fun deleteWithName(name: String)

    @Update
    suspend fun updatePlace(coordinates: PlacesEntity)

    @Query("SELECT * FROM places WHERE name = :name LIMIT 1")
    suspend fun getPlaceByName(name: String): PlacesEntity?
}