package com.tanish.resturantmanagement.RoomData

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoResutrant {

    @Insert
    suspend fun insertData(customer: EntityResutrant)

    @Update
    suspend fun updatData(customer: EntityResutrant)

    @Delete
    suspend fun deletData(customer: EntityResutrant)

    @Query("SELECT * FROM Resturant")
    fun getAllCustomer(): Flow<List<EntityResutrant>>


}
