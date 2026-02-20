package com.tanish.resturantmanagement.ui.theme



import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tanish.resturantmanagement.R

@Composable
fun OrderSummaryScreen(navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Click  on button to see the bill",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))


        }

        // Back Button
        FloatingActionButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {

        }

        // Floating Button
        FloatingActionButton(
            onClick = {
                navController.navigate("billScreen")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(painter = painterResource(R.drawable.add),
                contentDescription = "")
        }
    }
}