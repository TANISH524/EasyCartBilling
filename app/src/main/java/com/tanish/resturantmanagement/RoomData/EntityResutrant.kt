package com.tanish.resturantmanagement.RoomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Resturant")
data class EntityResutrant(
    @PrimaryKey(autoGenerate = true) val Id: Int = 0,
    var Name: String,
    var contact: String
)
