package com.sanskar.unilink.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.sanskar.unilink.Routes
import com.sanskar.unilink.Resource
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.viewmodel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportItemScreen(
    navController: NavController,
    viewModel: ViewModel
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("lost") } // or "found"

    val lostItemState by viewModel.lostItemState.collectAsState()
    val foundItemState by viewModel.foundItemState.collectAsState()

    val isLoading = lostItemState is Resource.Loading || foundItemState is Resource.Loading

    // Handle success
    LaunchedEffect(lostItemState, foundItemState) {
        if (lostItemState is Resource.Success || foundItemState is Resource.Success) {
            Toast.makeText(context, "Item reported successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.REPORT) { inclusive = true }
                viewModel.resetLostItemState()
                viewModel.resetFoundItemState()

            }
        }
        if (lostItemState is Resource.Error) {
            Toast.makeText(context, (lostItemState as Resource.Error).exception.message ?: "Error", Toast.LENGTH_SHORT).show()
        }
        if (foundItemState is Resource.Error) {
            Toast.makeText(context, (foundItemState as Resource.Error).exception.message ?: "Error", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Report Item") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Type:")
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = type == "lost",
                    onClick = { type = "lost" }
                )
                Text("Lost")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = type == "found",
                    onClick = { type = "found" }
                )
                Text("Found")
            }

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val item = LostFoundItem(
                        title = title,
                        description = description,
                        location = location,
                        type = type,
                        userId = userId
                    )
                    if (type == "lost") {
                        viewModel.lostItem(item)
                    } else {
                        viewModel.foundItem(item)
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}
