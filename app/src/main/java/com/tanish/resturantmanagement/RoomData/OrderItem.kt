package com.tanish.resturantmanagement.RoomData

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var orderId: Long =0L,
    var orderItemName: String? = null,
    var orderPrice: Double = 0.0
)
