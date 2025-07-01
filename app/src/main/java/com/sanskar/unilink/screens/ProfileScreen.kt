package com.sanskar.unilink.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.Routes
import com.sanskar.unilink.viewmodel.ViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ViewModel = viewModel()
    val userState by viewModel.userProfileState.collectAsState()
    val signOutState by viewModel.signOutState.collectAsState()

    // Trigger once on screen load
    LaunchedEffect(Unit) {
        viewModel.getUserProfile()
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
            when (val result = userState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val user = result.data
                    Text("Name: ${user.name}")
                    Text("Email: ${user.email}")
                    Text("SIC: ${user.sic}")
                    Text("Year: ${user.year}")
                    Text("Semester: ${user.semester}")
                    Text("College: ${user.college}")
                }

                is Resource.Error -> {
                    Text(
                        "Error loading profile: ${result.exception.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Idle -> {
                    Text("Fetching profile...", modifier = Modifier.padding(8.dp))
                }
            }
            Button(onClick = { viewModel.signOut() }
            ) {
                Text("Sign Out")

            }
            when (signOutState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    navController.navigate(Routes.LOGIN) {
                    }
                }

                is Resource.Error -> {
                    Text(
                        "Error signing out: ${(signOutState as Resource.Error).exception.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Idle -> {
                    //Text("Sign Out", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
