package com.sanskar.unilink.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanskar.unilink.Resource
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.viewmodel.ViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navController: NavController,
    itemId: String,
    viewModel: ViewModel,
    type: String
) {

    val context = LocalContext.current
    val itemState = viewModel.itemState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getItem(itemId, type)
    }

    when (val state = itemState.value) {
        is Resource.Success -> {

       val item = state.data as LostFoundItem


            Scaffold(
                topBar = { TopAppBar(title = { Text("Item Details") }) }
            ) { padding ->

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text("Title: ${item.title}", style = MaterialTheme.typography.titleLarge)
                        Text("Description: ${item.description}", modifier = Modifier.padding(top = 8.dp))
                        Text("Location: ${item.location}", modifier = Modifier.padding(top = 4.dp))
                        Text("Type: ${item.type.uppercase()}", modifier = Modifier.padding(top = 4.dp))

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if ( type == "lost") {
                                    viewModel.UpdateLostItems(itemId, item)
                                } else {
                                    viewModel.UpdateFoundItems(itemId, item.copy(type = "deactivate"))
                                }
                                navController.navigateUp()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("deactivate")
                        }
                    }
                }
        }
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error -> {
            Toast.makeText(context, state.exception.message, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }



}
