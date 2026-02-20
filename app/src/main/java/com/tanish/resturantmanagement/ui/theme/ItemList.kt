package com.tanish.resturantmanagement.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.R
import com.tanish.resturantmanagement.RoomData.DatabaseResturant
import com.tanish.resturantmanagement.RoomData.EntityResutrant
import com.tanish.resturantmanagement.RoomData.FoodEntity
import kotlinx.coroutines.launch

@Composable
fun ItemList(navController: NavHostController) {

    val context = LocalContext.current
    val db = DatabaseResturant.getDatabase(context)
    val customerDao = db.customerDao()
    val orderDao = db.orderDao()
    val foodDao = db.foodDao()

    val scope = rememberCoroutineScope()

    // Collect customers
    val allOrder by orderDao.getAllOrders().collectAsState(initial = emptyList())

    // Collect food items
    val foodItems by foodDao.getAllFood().collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(allOrder.size) { customer ->

                // Calculate total price for this customer (for simplicity sum of all food)


                // Single Card per customer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate("billScreen/${allOrder[customer].customerName}/${allOrder[customer].totalPrice}/${allOrder[customer].id}/${allOrder[customer].customerContact}")
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = allOrder[customer].customerName ?: "N/A")
                        Text(text = "Total: ₹"+allOrder[customer].totalPrice.toString())
                    }
                }
            }
        }

        // Floating Action Button bottom-right
        FloatingActionButton(
            onClick = {
                navController.navigate("Dropdown")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(painter = painterResource(R.drawable.add), contentDescription = "")
        }
    }
}