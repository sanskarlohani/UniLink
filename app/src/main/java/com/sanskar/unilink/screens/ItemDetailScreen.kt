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
import com.sanskar.unilink.models.LostFoundItem
import com.sanskar.unilink.viewmodel.ViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navController: NavController,
    itemId: String,
    viewModel: ViewModel
) {
    var item by remember { mutableStateOf<LostFoundItem?>(null) }
    val context = LocalContext.current

    LaunchedEffect(itemId) {
        FirebaseFirestore.getInstance().collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { document ->
                item = document.toObject(LostFoundItem::class.java)
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Item Details") }) }
    ) { padding ->
        item?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("Title: ${it.title}", style = MaterialTheme.typography.titleLarge)
                Text("Description: ${it.description}", modifier = Modifier.padding(top = 8.dp))
                Text("Location: ${it.location}", modifier = Modifier.padding(top = 4.dp))
                Text("Type: ${it.type.uppercase()}", modifier = Modifier.padding(top = 4.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        FirebaseFirestore.getInstance().collection("items")
                            .document(it.id)
                            .update("status", "claimed")
                        FirebaseDatabase.getInstance().reference
                            .child("liveItems").child(it.id)
                            .child("status").setValue("claimed")
                        Toast.makeText(context, "Item claimed!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Claim")
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
