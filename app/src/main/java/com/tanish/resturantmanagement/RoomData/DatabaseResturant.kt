package com.tanish.resturantmanagement.RoomData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EntityResutrant::class,
        FoodEntity::class,
        OrderEntity::class,
        OrderItem::class
    ],
    version = 3 ,  //  version 2 se 3 kar diya,
    exportSchema = false
)
abstract class DatabaseResturant : RoomDatabase() {

    abstract fun customerDao(): DaoResutrant
    abstract fun foodDao(): DaoBill
    abstract fun orderDao(): OrderDao   // new DAO added

    companion object {
        @Volatile
        private var INSTANCE: DatabaseResturant? = null

        fun getDatabase(context: Context): DatabaseResturant {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseResturant::class.java,
                    "Resturant"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
