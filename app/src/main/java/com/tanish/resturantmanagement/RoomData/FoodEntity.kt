package com.tanish.resturantmanagement.RoomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FoodItem")

data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
      val id :Int=0,
      var food :String?=null,
       var price:Double =0.0

)