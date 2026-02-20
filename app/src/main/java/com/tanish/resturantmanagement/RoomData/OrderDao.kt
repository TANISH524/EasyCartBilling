package com.tanish.resturantmanagement.RoomData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("SELECT * FROM Orders")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert
   suspend  fun  insertAllOrder(orderItem: OrderItem)

    @Query("SELECT * FROM Orders ORDER BY id DESC LIMIT 1")
    fun getLastOrder(): Flow<OrderEntity?>
    @Query("SELECT * FROM Orders WHERE customerName = :name ORDER BY id DESC LIMIT 1")
    fun getOrderByCustomer(name: String): Flow<OrderEntity?>

    @Query("SELECT * FROM OrderItem WHERE orderId = :id")
    fun getOrderItemsByOrderId(id: Long): Flow<List<OrderItem>>

}
