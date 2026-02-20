package com.tanish.resturantmanagement.RoomData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao

interface DaoBill{
    @Insert
    suspend fun insertFood(Food: FoodEntity)

    @Delete
    suspend fun deletFood(Food: FoodEntity)

    @Update
    suspend fun updateFood(Food: FoodEntity)

    @Query("SELECT * FROM FoodItem")
    fun getAllFood(): Flow<List<FoodEntity>>

}
