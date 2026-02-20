package com.tanish.resturantmanagement.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.RoomData.DatabaseResturant

@Composable
fun BillScreen(
    navController: NavHostController,
    customerName: String,
    totalPrice: String,
    orderId: String,
    contact: String
) {

    val context = LocalContext.current
    val db = DatabaseResturant.getDatabase(context)
    val customerDao = db.customerDao()
    val orderDao = db.orderDao()

    // Fetch customer info by name

    val orderItem by orderDao.getOrderItemsByOrderId(orderId.toLong()).collectAsState(emptyList())


    // Fetch all food items

    // Total price

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Customer info
        Text("Customer: ${customerName}")
        Text("Contact: ${contact}")

        Spacer(modifier = Modifier.height(16.dp))

        // Items list
        Text("Items:")
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(orderItem.size) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(orderItem[item].orderItemName.toString())
                    Text("₹ ${orderItem[item].orderPrice}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total
        Text("Total: ₹ $totalPrice")
    }
}