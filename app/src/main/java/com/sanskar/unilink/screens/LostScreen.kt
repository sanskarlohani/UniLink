package com.sanskar.unilink.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.Routes
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.models.User
import com.sanskar.unilink.viewmodel.ViewModel

@Composable
fun LostScreen(
    navController: NavController,
    viewModel: ViewModel
) {
    val lostListState by viewModel.lostListState.collectAsState()
    val userState by viewModel.userProfileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getLostItems()
        viewModel.getUserProfile()
    }

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

            // Show user profile errors (if needed)
            if (userState is Resource.Error) {
                Text(
                    text = "User error: ${(userState as Resource.Error).exception.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when (lostListState) {
                is Resource.Idle -> {
                    // Optional: initial idle placeholder
                }

                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${(lostListState as Resource.Error).exception.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is Resource.Success -> {
                    val itemsList = (lostListState as Resource.Success<List<LostFoundItem>>).data
                    val userEmail = (userState as? Resource.Success<User>)?.data?.email ?: ""

                    if (itemsList.isEmpty()) {
                        Text("No lost items found.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn {
                            items(itemsList) { item ->
                                ItemCard(
                                    item = item,
                                    userEmail = userEmail,
                                    onClick = {
                                        navController.navigate("${Routes.ITEM_DETAILS}/${item.id}/lost")
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
