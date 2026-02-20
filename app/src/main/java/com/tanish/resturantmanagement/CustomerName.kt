package com.tanish.resturantmanagement

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.RoomData.DatabaseResturant
import com.tanish.resturantmanagement.RoomData.EntityResutrant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Customer(navController: NavHostController) {

    val context = LocalContext.current
    val db = DatabaseResturant.getDatabase(context)
    val dao = db.customerDao()

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var customerList by remember { mutableStateOf(listOf<EntityResutrant>()) }

    var  isError by remember { mutableStateOf(false) }// phone validation

    // For update
    var updateCustomer by remember { mutableStateOf<EntityResutrant?>(null) }

    // Collect Room DB
    LaunchedEffect(Unit) {
        dao.getAllCustomer().collect { list ->
            customerList = list
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = {
                updateCustomer = null
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
            items(customerList) { customer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
//                        .clickable {
//                            Toast.makeText(context, "Bill screen", Toast.LENGTH_SHORT).show()
//                            navController.navigate("Bill")
//                        }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Name: ${customer.Name}")
                        Text("Contact: ${customer.contact}")

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {

                            Text(
                                text = "Update",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    updateCustomer = customer
                                    showDialog = true
                                }
                            )

                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        dao.deletData(customer)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog for Add / Update
    if (showDialog) {
        var name by remember { mutableStateOf(updateCustomer?.Name ?: "") }
        var contact by remember { mutableStateOf(updateCustomer?.contact ?: "") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (updateCustomer == null) "Add Customer" else "Update Customer") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Enter Name") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        singleLine = true

                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it.filter { char -> char.isDigit() }
                            isError = contact.length != 10 },
                        label = { Text("Enter Contact")

                        },
                        isError = isError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                    if (isError) {
                        Text(
                            text = "Phone number must be 10 digits",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Text(
                    text = if (updateCustomer == null) "Save" else "Update",
                    modifier = Modifier.clickable {
                        if (name.isNotEmpty() && contact.isNotEmpty()) {
                            scope.launch {
                                if (updateCustomer == null) {
                                    dao.insertData(EntityResutrant(Name = name, contact = contact))
                                } else {
                                    dao.updatData(updateCustomer!!.copy(Name = name, contact = contact))
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
