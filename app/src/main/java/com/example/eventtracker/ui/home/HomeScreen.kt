package com.example.eventtracker.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eventtracker.R
import com.example.eventtracker.model.EventData
import com.example.eventtracker.ui.theme.EventTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onEventClick: (EventData) -> Unit,
    viewModel: HomeScreenViewModel
) {

    HomeBody(modifier = modifier, viewModel = viewModel, onEventClick = onEventClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    viewModel: HomeScreenViewModel
) {
    val isSearchOn: State<Boolean> = viewModel.isSearchOn.collectAsState()
    val searchQuery: State<String> = viewModel.searchQuery.collectAsState()

    if (isSearchOn.value) {
        CenterAlignedTopAppBar(
            title = {
                TextField(
                    value = searchQuery.value, onValueChange = {
                        viewModel.updateSearchQuery(
                            it
                        )
                    }, modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Search events") },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search events",
                            modifier = Modifier.clickable { viewModel.toggleSearch() }
                        )
                    }
                )
            },
        )
    } else {
        CenterAlignedTopAppBar(
            title = { Text(text = "Events", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search events",
                    modifier = Modifier.clickable { viewModel.toggleSearch() }
                )
            },
            actions = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.clickable { onClickAction() }
                )
            },
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
// TopAppBar

}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    onEventClick: (EventData) -> Unit
) {

    Column(modifier = modifier.padding(horizontal = 16.dp)) {

        if (viewModel.isSearchOn.collectAsState().value)
            Spacer(modifier = Modifier.height(16.dp))
        SelectDateRangePreferenceBar()
        Spacer(modifier = Modifier.height(16.dp))
        if (viewModel.isSearching.collectAsState().value) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Text(
                text = "Showing Results for",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            EventList(
                eventList = viewModel.eventList.collectAsState().value,
                onEventClick = onEventClick,
                onInterestedAction = viewModel::onInterestedClicked,
                checkIfBookmarked = viewModel::checkIfBookmarked,
                viewModel = viewModel
            )
        }

    }
}

@Composable
fun EventList(
    modifier: Modifier = Modifier,
    eventList: List<EventData>,
    onEventClick: (EventData) -> Unit,
    onInterestedAction: suspend (EventData, onSuccess: () -> Unit, onFailure: () -> Unit) -> Unit,
    checkIfBookmarked: (event: EventData) -> Boolean,
    viewModel: HomeScreenViewModel
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(eventList) { index, item ->
            EventListItem(
                eventImage = item.image,
                eventTitle = item.name,
                eventDescription = item.description,
                eventDate = item.date,
                eventTime = item.time,
                eventLocation = item.location,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClickAction = onEventClick,
                event = item,
                onInterestedAction = onInterestedAction,
                checkIfBookmarked = checkIfBookmarked,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EventListItem(
    modifier: Modifier = Modifier,
    eventImage: String,
    eventTitle: String,
    eventDescription: String,
    eventDate: String,
    onClickAction: (EventData) -> Unit = {},
    eventTime: String,
    eventLocation: String,
    onInterestedAction: suspend (EventData,() -> Unit, () -> Unit) -> Unit,
    event: EventData,
    viewModel: HomeScreenViewModel,

    checkIfBookmarked: (event: EventData) -> Boolean = { false }
) {
    val bookmarked = viewModel.bookmarkedEvents.collectAsState()
    val isBookmarked = bookmarked.value.any { it.eventId == event.eventId }
    val context = LocalContext.current
//    isBookmarked = checkIfBookmarked(event)
    Column(modifier = modifier.clickable { onClickAction(event) }) {
        AsyncImage(
            model = event.eventImageUrl,
            error = painterResource(id = R.drawable.default_image),
            contentDescription = "Event Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(300.dp)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = eventTitle, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "$eventDate, $eventTime")
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = eventLocation, modifier = Modifier.weight(0.7f))
        Button(
            onClick =
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.onInterestedClicked(
                            event,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Event Bookmarked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, onFailure = {
                                Toast.makeText(
                                    context,
                                    "Event UnBookmarked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                    // Update state after Firebase task}
                    //               isBookmarked =  onInterestedAction(event, isBookmarked)
                    //                Log.d("isBookmarked", isBookmarked.toString())
                },

            colors = ButtonDefaults.buttonColors(
                if (isBookmarked)
                    Color(
                        176,
                        183,
                        192,
                        70
                    ) else Color.Unspecified
            ),

            ) {

            Text(
                text = if (isBookmarked) "Unbookmark" else "Bookmark",
                color = if (isBookmarked) Color.Gray else Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun SelectDateRangePreferenceBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        SelectDateRangePreferenceBarItem(text = "Today")
        SelectDateRangePreferenceBarItem(text = "Tomorrow")
        SelectDateRangePreferenceBarItem(text = "This Week")
        SelectDateRangePreferenceBarItem(text = "This Month")
    }
}

@Composable
fun SelectDateRangePreferenceBarItem(
    modifier: Modifier = Modifier,
    text: String,
    onClickAction: () -> Unit = {}
) {
    Button(
        onClick = onClickAction,
        modifier = modifier.padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(Color(176, 183, 192, 70)),
        shape = MaterialTheme.shapes.large
    ) {
        Text(text = text, color = Color.Black)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    EventTrackerTheme {

    }
}