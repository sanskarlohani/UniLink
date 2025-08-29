package com.sanskar.unilink.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sanskar.unilink.Resource
import com.sanskar.unilink.components.BottomNavBar
import com.sanskar.unilink.components.ShimmerItemList
import com.sanskar.unilink.components.ShimmerTabLayout
import com.sanskar.unilink.components.TabLayout
import com.sanskar.unilink.viewmodel.ViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel1: ViewModel) {
    // Use the passed viewModel instead of creating a new one
    val viewModel = viewModel1
    var selectedTab by remember { mutableIntStateOf(0) }

    // Pull-to-refresh state
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    // Monitor loading states for pull-to-refresh
    val userState by viewModel.userProfileState.collectAsState()
    val lostState by viewModel.lostListState.collectAsState()
    val foundState by viewModel.foundListState.collectAsState()

    // Load data only once when the screen is first created
    LaunchedEffect(Unit) {
        viewModel.getUserProfileWithCache()
        viewModel.getLostItemsWithCache()
        viewModel.getFoundItemsWithCache()
    }

    // Update refresh state based on loading states (only for pull-to-refresh)
    LaunchedEffect(userState, lostState, foundState) {
        isRefreshing = userState is Resource.Loading ||
                     lostState is Resource.Loading ||
                     foundState is Resource.Loading
    }

    // Check if we should show shimmer (initial loading, not refresh)
    val showShimmer = (lostState is Resource.Loading && foundState is Resource.Loading) ||
                      (lostState is Resource.Idle && foundState is Resource.Idle)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UniLink") }
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->

        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    viewModel.refreshAllData()
                }
            },
            modifier = Modifier.padding(paddingValues)
        ) {
            if (showShimmer) {
                // Show shimmer effect during initial loading
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ShimmerTabLayout()
                    Spacer(modifier = Modifier.height(12.dp))
                    ShimmerItemList(itemCount = 6)
                }
            } else {
                // Show actual content
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tab Layout with 0 = Found, 1 = Lost
                    TabLayout(selectedTabIndex = selectedTab) {
                        selectedTab = it
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (selectedTab) {
                        1 -> LostScreen(navController = navController, viewModel = viewModel)
                        0 -> FoundScreen(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
