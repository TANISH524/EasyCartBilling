package com.tanish.resturantmanagement

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.RoomData.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db = DatabaseResturant.getDatabase(context)
    val customerDao = db.customerDao()
    val foodDao = db.foodDao()
    val scope = rememberCoroutineScope()

    var cartItems = remember { mutableStateListOf<FoodEntity?> ()}
    var showDialog by remember { mutableStateOf(false) }

    val customers by customerDao.getAllCustomer().collectAsState(initial = emptyList())
    val foods by foodDao.getAllFood().collectAsState(initial = emptyList())

    var expandedCustomer by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf("") }

    var expandedFood by remember { mutableStateOf(false) }
    var selectedFood: String? by remember { mutableStateOf("") }
    var selectedPrice: String? by remember { mutableStateOf("") }

    var totalPrie by remember { mutableDoubleStateOf(0.0) }

    var orderitem by remember { mutableStateOf<OrderItem?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 70.dp) // space bottom buttoon ko
        ) {

            // ================== CUSTOMER DROPDOWN ==================
            ExposedDropdownMenuBox(
                expanded = expandedCustomer,
                onExpandedChange = { expandedCustomer = !expandedCustomer }
            ) {
                OutlinedTextField(
                    value = selectedCustomer,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Customer") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCustomer) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedCustomer,
                    onDismissRequest = { expandedCustomer = false }
                ) {
                    if (customers.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No customers available") },
                            onClick = { expandedCustomer = false }
                        )
                    } else {
                        customers.forEach { c ->
                            DropdownMenuItem(
                                text = { Text("${c.Name} - ${c.contact}") },
                                onClick = {
                                    selectedCustomer = c.Name
                                    selectedContact =c.contact
                                    expandedCustomer = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ================== CART ITEMS LIST ==================
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                items(cartItems.size) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(cartItems[item]?.food.toString(), modifier = Modifier.weight(1f))

                        Text(" ${cartItems[item]?.price}")
                    }
                }
            }



            Text("Total:  ${"%.2f".format(totalPrie)}", modifier = Modifier.padding(8.dp))
        }

        // ================== ADD FOOD BUTTON ==================
        Button(
            onClick = {
                if (selectedCustomer.isNotBlank() && cartItems.isNotEmpty()) {

//                    val total = cartItems.sumOf { it?.price?.toInt() }

                    val itemsString = cartItems.joinToString(",") { it?.food.toString() }

                    // Name aur Contact split karo
//                    val parts = selectedCustomer.split(" - ")


                    scope.launch {
                     val oId = withContext(Dispatchers.IO) {
                            db.orderDao().insertOrder(
                                OrderEntity(
                                    customerName = selectedCustomer,
                                    customerContact = selectedContact,
                                    totalPrice = totalPrie
                                )
                            )
                        }

                        println("Check cartList: ${oId.toString()}")

                        for(i in cartItems){

                            val orderItem = OrderItem()
                            if(i!=null) {
                                orderItem.orderId = oId
                                orderItem.orderPrice = i.price
                                orderItem.orderItemName = i.food
                            }

                            println("Check OrderItem: ${orderItem}")

                            scope.launch {
                                withContext(Dispatchers.IO){
                                    db.orderDao().insertAllOrder(orderItem)
                                }


    }
                        }

                        Toast.makeText(context,"bill generated ", Toast.LENGTH_SHORT).show()

                        navController.navigate("orderSummary")
                    }


                    // Reset after saving
                  //  cartItems.clear()

                }



                      },/////////// main code ADD BUTTON DA
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Food")
        }

        // ================== FLOATING ACTION BUTTON ==================
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp) // above the Add Food button
        ) {
            Icon(painter = painterResource(R.drawable.add),
                contentDescription = "")
        }
    }

    // ================== ADD FOOD DIALOG ==================
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Food Item") },
            text = {
                Column {
                    // FOOD DROPDOWN
                    ExposedDropdownMenuBox(
                        expanded = expandedFood,
                        onExpandedChange = { expandedFood = !expandedFood },
                    ) {
                        selectedFood?.let {
                            OutlinedTextField(
                                value = it,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Food") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFood) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = expandedFood,
                            onDismissRequest = { expandedFood = false }
                        ) {
                            if (foods.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No food available") },
                                    onClick = { expandedFood = false }
                                )
                            } else {
                                foods.forEach { f ->
                                    DropdownMenuItem(
                                        text = { Text("${f.food} - ${f.price}") },
                                        onClick = {
                                            selectedFood = f.food
                                            selectedPrice = f.price.toString()
                                            expandedFood = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // PRICE FIELD
                    selectedPrice?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Price") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val price = selectedPrice?.toDoubleOrNull()
                    if (selectedFood?.isNotBlank() == true && price != null) {
                       cartItems.add(FoodEntity(food = selectedFood, price = selectedPrice!!.toDouble()))
                        selectedFood = ""
                        selectedPrice = ""
                        showDialog = false
                        totalPrie += price
                    }
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
