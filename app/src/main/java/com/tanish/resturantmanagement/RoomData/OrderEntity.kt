package com.tanish.resturantmanagement.RoomData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val customerName: String?=null,
    val customerContact: String?=null,
    val totalPrice: Double=0.0
)
