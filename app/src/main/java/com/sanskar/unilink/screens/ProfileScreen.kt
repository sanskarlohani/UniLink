package com.sanskar.unilink.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sanskar.unilink.models.User
import com.sanskar.unilink.screens.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
                .get()
                .addOnSuccessListener { doc ->
                    user = doc.toObject(User::class.java)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            user?.let {
                Text("Name: ${it.name}")
                Text("Email: ${it.email}")
                Text("SIC: ${it.sic}")
                Text("Year: ${it.year}")
                Text("Semester: ${it.semester}")
                Text("College: ${it.college}")
            } ?: CircularProgressIndicator()
        }
    }
}
