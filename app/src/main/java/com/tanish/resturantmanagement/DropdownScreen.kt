package com.tanish.resturantmanagement.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.R
import com.tanish.resturantmanagement.RoomData.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownScreen(
    navController: NavHostController,
    database: DatabaseResturant
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // ================= CUSTOMER DATA =================
    val customers by database.customerDao()
        .getAllCustomer()
        .collectAsState(initial = emptyList())

    var expandedCustomer by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<EntityResutrant?>(null) }

    // ================= FOOD DATA =================
    val foodItems by database.foodDao()
        .getAllFood()
        .collectAsState(initial = emptyList())

    var expandedFood by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<FoodEntity?>(null) }

    // ================= ORDER ITEMS =================
    var orderItems by remember { mutableStateOf(listOf<OrderItem>()) }

    // ================= DIALOG STATE =================
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ================= CUSTOMER DROPDOWN =================
        Text("Select Customer", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedCustomer,
            onExpandedChange = { expandedCustomer = !expandedCustomer }
        ) {

            TextField(
                value = selectedCustomer?.let {
                    "${it.Name} - ${it.contact}"
                } ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select Customer") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCustomer)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCustomer,
                onDismissRequest = { expandedCustomer = false }
            ) {
                if (customers.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No Customers Found") },
                        onClick = { expandedCustomer = false }
                    )
                } else {
                    customers.forEach { customer ->
                        DropdownMenuItem(
                            text = {
                                Text("${customer.Name} - ${customer.contact}")
                            },
                            onClick = {
                                selectedCustomer = customer
                                expandedCustomer = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= ORDER ITEMS LIST =================
        Text("Order Items", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(orderItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.orderItemName ?: "")
                    Text("₹ ${item.orderPrice}")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= TOTAL PRICE =================
        val totalPrice = orderItems.sumOf { it.orderPrice }

        Text(
            text = "Total: ₹ $totalPrice",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ================= FLOATING BUTTON =================
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add Item"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= ADD ORDER BUTTON =================
        Button(
            onClick = {
                if (selectedCustomer != null && orderItems.isNotEmpty()) {

                    scope.launch {
                        val orderId = database.orderDao().insertOrder(
                            OrderEntity(
                                customerName = selectedCustomer!!.Name,
                                customerContact = selectedCustomer!!.contact,
                                totalPrice = totalPrice
                            )
                        )

                        orderItems.forEach { it.orderId = orderId }
                        orderItems.forEach {
                            database.orderDao().insertAllOrder(it)
                        }

                        Toast.makeText(context, "Order Saved", Toast.LENGTH_SHORT).show()

//                        selectedCustomer = null
//                        orderItems = emptyList()
                    }
                } else {
                    Toast.makeText(context, "Select Customer & Add Items", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Order")
        }
    }

    // ================= FOOD DIALOG =================
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Select Food Item", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedFood,
                        onExpandedChange = { expandedFood = !expandedFood }
                    ) {

                        TextField(
                            value = selectedFood?.food ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select Food") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFood)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFood,
                            onDismissRequest = { expandedFood = false }
                        ) {
                            if (foodItems.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No Food Found") },
                                    onClick = { expandedFood = false }
                                )
                            } else {
                                foodItems.forEach { food ->
                                    DropdownMenuItem(
                                        text = {
                                            Text("${food.food} - ₹${food.price}")
                                        },
                                        onClick = {
                                            selectedFood = food
                                            expandedFood = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Price: ₹ ${selectedFood?.price ?: 0.0}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            selectedFood?.let {
                                orderItems = orderItems + OrderItem(
                                    orderItemName = it.food,
                                    orderPrice = it.price
                                )
                                selectedFood = null
                                showDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Item")
                    }
                }
            }
        }
    }
}