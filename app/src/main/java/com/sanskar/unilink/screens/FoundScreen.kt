package com.sanskar.unilink.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.components.ItemCard
import com.sanskar.unilink.navigation.Routes
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.models.User
import com.sanskar.unilink.viewmodel.ViewModel

@Composable
fun FoundScreen(
    navController: NavController,
    viewModel: ViewModel
) {
    val foundListState by viewModel.foundListState.collectAsState()
    val userState by viewModel.userProfileState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.REPORT)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Optional: show user error if exists
            if (userState is Resource.Error) {
                Text(
                    text = "User error: ${(userState as Resource.Error).exception.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when (foundListState) {
                is Resource.Idle -> {
                    // Shimmer is handled in HomeScreen, show nothing here
                }

                is Resource.Loading -> {
                    // Remove CircularProgressIndicator - shimmer handles this in HomeScreen
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${(foundListState as Resource.Error).exception.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is Resource.Success -> {
                    val itemsList = (foundListState as Resource.Success<List<LostFoundItem>>).data
                    val userEmail = (userState as? Resource.Success<User>)?.data?.email ?: ""

                    if (itemsList.isEmpty()) {
                        Text("No found items yet.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn {
                            items(itemsList) { item ->
                                ItemCard(
                                    item = item,
                                    userEmail = userEmail,
                                    onClick = {
                                        navController.navigate("${Routes.ITEM_DETAILS}/${item.id}/found")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
