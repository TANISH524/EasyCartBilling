package com.tanish.resturantmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.tanish.resturantmanagement.RoomData.DatabaseResturant
import com.tanish.resturantmanagement.ui.theme.BillScreen
import com.tanish.resturantmanagement.ui.theme.DropdownScreen

import com.tanish.resturantmanagement.ui.theme.ItemList
import com.tanish.resturantmanagement.ui.theme.OrderSummaryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dashboard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route?.substringBefore("/") ?: "Customer"

    val selectedIndex = when (currentRoute) {
        "Customer" -> 0
        "FoodItem" -> 1
        "ItemList" -> 2
        else -> 0
    }

    val screenTitle = when (currentRoute) {
        "Customer" -> "Customer Details"
        "FoodItem" -> "Food Items"
        "ItemList" -> "Item List"
        else -> "Customer Details"
    }

    Scaffold(
        topBar = {
            if(currentRoute != "Dropdown" && currentRoute != "orderSummary" && currentRoute != "billScreen") {
                TopAppBar(
                    title = { Text(screenTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
                )
            }
        },
        bottomBar = {
            if(currentRoute != "Dropdown" && currentRoute != "orderSummary" && currentRoute != "billScreen") {
                BottomAppBar(containerColor = Color.DarkGray) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Customer Tab
                        Column(
                            modifier = Modifier.clickable {
                                navController.navigate("Customer") { launchSingleTop = true }
                            },
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.customer_detail),
                                contentDescription = "Customer",
                                modifier = Modifier.size(40.dp),
                                tint = if (selectedIndex == 0) Color.Red else Color.White
                            )
                            if (selectedIndex == 0) {
                                Text("Add Detail", color = Color.White)
                            }
                        }

                        // FoodItem Tab
                        Column(
                            modifier = Modifier.clickable {
                                navController.navigate("FoodItem") { launchSingleTop = true }
                            },
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.bill),
                                contentDescription = "FoodItem",
                                modifier = Modifier.size(40.dp),
                                tint = if (selectedIndex == 1) Color.Red else Color.White
                            )
                            if (selectedIndex == 1) {
                                Text("FoodItem", color = Color.White)
                            }
                        }

                        // Cart Tab
                        Column(
                            modifier = Modifier.clickable {
                                navController.navigate("ItemList") { launchSingleTop = true }
                            },
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.cart),
                                contentDescription = "Cart",
                                modifier = Modifier.size(40.dp),
                                tint = if (selectedIndex == 2) Color.Red else Color.White
                            )
                            if (selectedIndex == 2) {
                                Text("Cart", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Customer",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Customer") {
                Customer(navController)
            }
            composable("FoodItem") {
                FoodItem(navController)
            }

            composable("ItemList") {
                ItemList(navController)
            }


            composable(
                route = "billScreen/{customerName}/{totalPrice}/{id}/{contact}"
//                arguments = listOf(navArgument("customerName") {
//                    type = NavType.StringType
//
//                },

                //)
            ) { backStackEntry ->
                val customerName = backStackEntry.arguments?.getString("customerName") ?: ""
                val totalPrice = backStackEntry.arguments?.getString("totalPrice") ?: ""
                val orderId = backStackEntry.arguments?.getString("id") ?: ""
                val contact = backStackEntry.arguments?.getString("contact") ?: ""

                BillScreen(navController, customerName, totalPrice, orderId,contact)
            }


            composable("Dropdown") {
                            val context = LocalContext.current
                            val database = DatabaseResturant.getDatabase(context)

                            DropdownScreen(navController = navController, database = database)
                        }

            composable("orderSummary") {
                OrderSummaryScreen(navController)
            }
        }
    }
}



