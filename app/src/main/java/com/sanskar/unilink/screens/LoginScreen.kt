package com.sanskar.unilink.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.Routes
import com.sanskar.unilink.viewmodel.ViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: ViewModel ) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    var isLoading = loginState is Resource.Loading

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Welcome Back",
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    )
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if(!isLoading){
                        viewModel.login(email,password)
                    } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else{
                    Text(
                        text = "Login",
                        modifier = Modifier,
                        fontSize = 16.sp

                    )
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {navController.navigate(Routes.SIGNUP) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text( "Don't have an account? Create One" )
            }
        }

        when(loginState){
            is Resource.Idle -> {

            }
            is Resource.Success -> {
                navController.navigate(Routes.HOME)
            }
            is Resource.Error -> {
                Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
            is Resource.Loading -> {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            }
        }


    }
}

