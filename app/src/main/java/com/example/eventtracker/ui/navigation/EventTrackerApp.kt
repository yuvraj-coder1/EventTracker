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
import com.example.eventtracker.ui.home.HomeScreenViewModel
import com.example.eventtracker.ui.postNewEvent.PostNewEventScreen
import com.example.eventtracker.ui.profile.ProfileScreen
import com.example.eventtracker.ui.signIn.SignInScreen
import com.example.eventtracker.ui.signIn.SignInViewModel
import com.example.eventtracker.ui.userEventsScreen.UserEventsScreen

@Composable
fun EventTrackerApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    homeScreenViewModel: HomeScreenViewModel,
    signInViewModel: SignInViewModel,
) {
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = LogInScreen,
        modifier = modifier
    ) {
        composable<LogInScreen> {
            onBottomBarVisibilityChanged(false)
            SignInScreen(
                viewModel = signInViewModel,
                navigateToHome = {
                    navController.navigate(HomeScreen) {
                    popUpTo(LogInScreen) {
                        inclusive = true
                    }
                    }
                })
        }

        composable<HomeScreen> {
            onBottomBarVisibilityChanged(true)
            com.example.eventtracker.ui.home.HomeScreen(

                onEventClick = {
                    navController.navigate(
                        EventDetailsScreen(
                            name = it.name,
                            description = it.description,
                            date = it.date,
                            location = it.location,
                            time = it.time,
                            image = it.image,
                            category = it.category,
                            eventLink = it.eventLink
                        )
                    )
                },
                viewModel = homeScreenViewModel
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
                category = args.category,
                eventLink = args.eventLink
            )
            EventDetailScreen(
                event = event,
                viewModel = homeScreenViewModel
            )
        }
        composable<ProfileScreen> {
            onBottomBarVisibilityChanged(true)
            com.example.eventtracker.ui.profile.ProfileScreen(navigateToLogin = {
                signInViewModel.signOut()
                navController.navigate(LogInScreen)
            })
        }
        composable<PostEventScreen> {
            onBottomBarVisibilityChanged(true)
            PostNewEventScreen(getEvents = { homeScreenViewModel.getEvents() })
        }

        composable<UserEventsScreen> {
            onBottomBarVisibilityChanged(true)
            UserEventsScreen(
                onEventClick = {
                    navController.navigate(
                        EventDetailsScreen(
                            name = it.name,
                            description = it.description,
                            date = it.date,
                            location = it.location,
                            time = it.time,
                            image = it.image,
                            category = it.category
                        )
                    )
                },
            )
        }

    }
}