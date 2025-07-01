package com.sanskar.unilink.screens

import android.R.attr.label
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sanskar.unilink.Routes
import com.sanskar.unilink.models.LostFoundItem

@Composable
fun TabLayout(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Found", "Lost")

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(Routes.HOME) }
        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Report") },
//            label = { Text("Report") },
//            selected = false,
//            onClick = { navController.navigate(Routes.REPORT) }
//        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate(Routes.PROFILE) }
        )
    }
}


@Composable
fun CustomOutlinedTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text("Enter $label") },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
    )
}


@Composable
fun ItemCard(item: LostFoundItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.Companion.padding(16.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium)
            Text(item.description, maxLines = 2, overflow = TextOverflow.Companion.Ellipsis)
            Spacer(modifier = Modifier.Companion.height(4.dp))
            Text("Location: ${item.location}", style = MaterialTheme.typography.bodySmall)
        }
    }
}