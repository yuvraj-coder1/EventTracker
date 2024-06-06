package com.example.eventtracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.eventtracker.model.EventData
import com.example.eventtracker.ui.home.EventDetailScreen
import com.example.eventtracker.ui.home.HomeScreen
import com.example.eventtracker.ui.postNewEvent.PostNewEventScreen
import com.example.eventtracker.ui.profile.ProfileScreen

@Composable
fun EventTrackerApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = HomeScreen,
        modifier = modifier
    ) {
        composable<HomeScreen> {
            onBottomBarVisibilityChanged(true)
            com.example.eventtracker.ui.home.HomeScreen(

                onEventClick = { navController.navigate(EventDetailsScreen(
                    name = it.name,
                    description = it.description,
                    date = it.date,
                    location = it.location,
                    time = it.time,
                    image = it.image,
                    category = it.category
                )) }
            )
        }
        composable<EventDetailsScreen> {
            onBottomBarVisibilityChanged(false)
            val args = it.toRoute<EventDetailsScreen>()
            val event = EventData(
                name = args.name,
                description = args.description,
                date = args.date,
                location = args.location,
                time = args.time,
                image = args.image,
                category = args.category
            )
            EventDetailScreen(
                event = event,
            )
        }
        composable<ProfileScreen> {
            onBottomBarVisibilityChanged(true)
            com.example.eventtracker.ui.profile.ProfileScreen(name = "Yash Aggarwal")
        }
        composable<PostEventScreen> {
            onBottomBarVisibilityChanged(false)
            PostNewEventScreen()
        }
    }
}