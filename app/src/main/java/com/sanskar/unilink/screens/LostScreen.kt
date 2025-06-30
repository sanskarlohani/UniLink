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
import com.sanskar.unilink.Routes
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.screens.ItemCard
import com.sanskar.unilink.viewmodel.ViewModel

@Composable
fun LostScreen(
    navController: NavController,
    viewModel: ViewModel
) {
    val lostListState by viewModel.lostListState.collectAsState()

    // Trigger data load on first composition
    LaunchedEffect(Unit) {
        viewModel.getLostItems()
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
            when (lostListState) {
                is Resource.Idle -> {

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

                    if (itemsList.isEmpty()) {
                        Text("No lost items found.", modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn {
                            items(itemsList) { item ->
                                ItemCard(
                                    item = item,
                                    onClick = {  }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
