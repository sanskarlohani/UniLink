package com.sanskar.unilink.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.components.CustomOutlinedTextField
import com.sanskar.unilink.navigation.Routes
import com.sanskar.unilink.models.User
import com.sanskar.unilink.viewmodel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navigator: NavController, authViewModel: ViewModel){

    val context = LocalContext.current
    var signupState = authViewModel.signUpState.collectAsState()
    var isLoading = signupState.value is Resource.Loading


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var sic by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Your Account",
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        fontSize = 20.sp,
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.Center
        ) {
            CustomOutlinedTextField(text = name, label = "Name", onValueChange = { name = it })
            CustomOutlinedTextField(text = email, "email", onValueChange = { email = it })
            CustomOutlinedTextField(text = sic, "sic", onValueChange = { sic = it })
            CustomOutlinedTextField(text = year, "year", onValueChange = { year = it })
            CustomOutlinedTextField(text = semester, "semester", onValueChange = { semester = it })
            CustomOutlinedTextField(text = college, "college", onValueChange = { college = it })
            CustomOutlinedTextField(text = password, "password", onValueChange = { password = it })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (!isLoading){
                    authViewModel.signUp(
                        User(
                            name = name,
                            email = email,
                            sic = sic,
                            year = year,
                            semester = semester,
                            college = college
                        ),
                        password
                    )
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),

            ) {
                if (isLoading) {
                    Text("Loading...")
                } else
                    Text(
                        text = "Sign Up",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.dp.value.sp,
                        ),
                        modifier = Modifier,
                        fontSize = 16.sp
                    )

            }
        }
    }

    when(signupState.value){
        is Resource.Idle -> {

        }
        is Resource.Success -> {
            navigator.navigate(Routes.HOME)
        }
        is Resource.Error -> {
            Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
    }
        is Resource.Loading -> {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }
    }


}