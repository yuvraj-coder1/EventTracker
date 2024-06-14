package com.example.eventtracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eventtracker.ui.bottomBar.BottomBar
import com.example.eventtracker.ui.home.EventDetailScreen
import com.example.eventtracker.ui.home.EventDetailScreenTopBar
import com.example.eventtracker.ui.home.HomeScreenTopBar
import com.example.eventtracker.ui.home.HomeScreenViewModel
import com.example.eventtracker.ui.navigation.EventDetailsScreen
import com.example.eventtracker.ui.navigation.EventTrackerApp
import com.example.eventtracker.ui.navigation.HomeScreen
import com.example.eventtracker.ui.navigation.PostEventScreen
import com.example.eventtracker.ui.navigation.ProfileScreen
import com.example.eventtracker.ui.navigation.UserEventsScreen
import com.example.eventtracker.ui.postNewEvent.PostNewEventScreenTopBar
import com.example.eventtracker.ui.profile.ProfileScreenTopBar
import com.example.eventtracker.ui.signIn.SignInViewModel
import com.example.eventtracker.ui.theme.EventTrackerTheme
import com.example.eventtracker.ui.userEventsScreen.UserEventsScreenTopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventTrackerTheme {
                val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
                val signInViewModel: SignInViewModel = hiltViewModel()
                val navController = rememberNavController()
                var buttonsVisible by remember { mutableStateOf(false) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val pathString = "com.example.eventtracker.ui.navigation."
                val eventDetailScreenPath =
                    "com.example.eventtracker.ui.navigation." +
                            "EventDetailsScreen?name={name}&image={image}&" +
                            "date={date}&time={time}&location={location}&description" +
                            "={description}&category={category}"
                Log.d("route", navBackStackEntry?.destination?.route.toString())
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Show bottom bar only if buttonsVisible is true
                        // Show bottom bar only if buttonsVisible is true
                        if (buttonsVisible) {
                            BottomBar(
                                navController = navController,
                                state = buttonsVisible,
                                modifier = Modifier,
                            )
                        }
                    },
                    topBar = {
                        when (navBackStackEntry?.destination?.route) {

                            pathString + HomeScreen.toString() -> HomeScreenTopBar(
                                onClickAction = {},
                                viewModel = homeScreenViewModel
                            )

                            eventDetailScreenPath -> EventDetailScreenTopBar()
                            pathString + PostEventScreen.toString() -> PostNewEventScreenTopBar(
                                onClickAction = { navController.navigate(HomeScreen)})

                            pathString + ProfileScreen.toString() -> ProfileScreenTopBar()
                            pathString + UserEventsScreen.toString() -> UserEventsScreenTopBar()
                            else -> {
                                Log.d("route", "I am here")
                                //DO NOTHING
                            }
                        }
                    }
                ) { innerPadding ->
                    EventTrackerApp(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onBottomBarVisibilityChanged = { buttonsVisible = it },
                        homeScreenViewModel = homeScreenViewModel,
                        signInViewModel = signInViewModel
                    )
                }
            }
        }
    }
}

