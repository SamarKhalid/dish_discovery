package com.example.dishdiscovery.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.dishdiscovery.services.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @androidx.room.Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>
}