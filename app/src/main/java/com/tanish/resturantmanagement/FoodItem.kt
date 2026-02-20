package com.tanish.resturantmanagement

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.RoomData.DatabaseResturant
import com.tanish.resturantmanagement.RoomData.FoodEntity
import com.tanish.resturantmanagement.RoomData.DaoBill
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItem(navController: NavHostController) {

    val context = LocalContext.current
    val db = DatabaseResturant.getDatabase(context)
    val dao: DaoBill = db.foodDao() // Correct DAO for food

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var foodList by remember { mutableStateOf(listOf<FoodEntity>()) }

    // For update
    var updateFood by remember { mutableStateOf<FoodEntity?>(null) }



    // Collect Room DB
    LaunchedEffect(Unit) {
        dao.getAllFood().collect { list ->
            foodList = list
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = {
                updateFood = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(foodList) { food ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Food: ${food.food}")
                        Text("Price: ${food.price}")

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {

                            Text(
                                text = "Update",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    updateFood = food
                                    showDialog = true
                                }
                            )

                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        dao.deletFood(food)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog for Add / Update Food
    if (showDialog) {
        var name by remember { mutableStateOf(updateFood?.food ?: "") }
        var price by remember { mutableStateOf(updateFood?.price ?.toString()?: "") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (updateFood == null) "Add Food" else "Update Food") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Enter Food Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Enter Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true
                    )
                }
            },
            confirmButton = {
                Text(
                    text = if (updateFood == null) "Save" else "Update",
                    modifier = Modifier.clickable {
                        if (name.isNotEmpty() && price.isNotEmpty()) {
                            scope.launch {
                                val priceDouble = price.toDoubleOrNull() ?: 0.0
                                if (updateFood == null) {

                                    dao.insertFood(FoodEntity(food = name, price = priceDouble))
                                } else {
                                    dao.updateFood(updateFood!!.copy(food = name, price = priceDouble))
                                }
                            }
                        }
                        showDialog = false
                    }
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable { showDialog = false }
                )
            }
        )
    }
}
