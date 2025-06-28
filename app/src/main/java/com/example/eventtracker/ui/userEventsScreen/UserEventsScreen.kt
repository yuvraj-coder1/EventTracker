package com.example.eventtracker.ui.userEventsScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.eventtracker.R
import com.example.eventtracker.model.EventData

@Composable
fun UserEventsScreen(
    modifier: Modifier = Modifier,
    onEventClick: (EventData) -> Unit,
    bookmarkedEvents: List<EventData> = emptyList<EventData>(),
    hostedEvents: List<EventData> = emptyList<EventData>()
) {
    val viewModel: UserEventsScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
    ) {
        EventsToShow(
            isBookmarksSelected = uiState.isBookmarksSelected,
            onClick = { viewModel.updateIsBookmarkedSelected(it) })
        Spacer(modifier = Modifier.height(16.dp))
        EventList(
            eventList = if (uiState.isBookmarksSelected)
                bookmarkedEvents else
                hostedEvents,
            onEventClick = onEventClick
        )
    }
}

@Composable
fun EventList(
    modifier: Modifier = Modifier,
    eventList: List<EventData> = emptyList(),
    onEventClick: (EventData) -> Unit
) {
    LazyColumn {
        itemsIndexed(eventList) { _, item ->
            EventListItem(
                eventTitle = item.name,
                eventImage = item.image,
                eventDate = item.date,
                eventLocation = item.location,
                onEventClick = onEventClick,
                event = item
            )
        }
    }
}

@Composable
fun EventListItem(
    modifier: Modifier = Modifier,
    eventTitle: String = "AI Debate: The AGI debate",
    eventImage: String = "",
    eventDate: String = "Dec 23, 2:00pm",
    eventLocation: String = "ISE department",
    onEventClick: (EventData) -> Unit = {},
    event: EventData
) {
    Row(
        modifier = modifier
            .padding(12.dp)
            .clickable { onEventClick(event) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = eventImage,
            error = painterResource(id = R.drawable.default_image),
            contentDescription = "Event Image",
            modifier = Modifier
                .height(80.dp)
                .weight(0.5f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = eventTitle,
                fontWeight = FontWeight.W500,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = eventLocation, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = eventDate, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun EventsToShow(
    modifier: Modifier = Modifier,
    isBookmarksSelected: Boolean = true,
    onClick: (Boolean) -> Unit = {}
) {
    val bookmarkWeight by animateFloatAsState(if (isBookmarksSelected) 1.4f else 1f)
    val hostedWeight by animateFloatAsState(if (isBookmarksSelected) 1f else 1.4f)
    val bookmarkColor by animateColorAsState(
        if (isBookmarksSelected) Color.White else Color(
            240,
            242,
            245
        )
    )
    val hostedColor by animateColorAsState(
        if (!isBookmarksSelected) Color.White else Color(
            240,
            242,
            245
        )
    )
    val bookmarkElevation by animateDpAsState(if (isBookmarksSelected) 4.dp else 0.dp)
    val hostedElevation by animateDpAsState(if (!isBookmarksSelected) 4.dp else 0.dp)
    val bookmarkTextColor by animateColorAsState(if (isBookmarksSelected) Color.Black else Color.Gray)
    val hostedTextColor by animateColorAsState(if (!isBookmarksSelected) Color.Black else Color.Gray)
    Row(
        modifier = modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .background(color = Color(240, 242, 245))


    ) {
        Button(
            onClick = { onClick(true) },
            modifier = Modifier
                .weight(bookmarkWeight)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = bookmarkColor
            ),
            elevation = ButtonDefaults.buttonElevation(bookmarkElevation),
            shape = MaterialTheme.shapes.large

        ) {
            Text(text = "Bookmarked", color = bookmarkTextColor)
        }
        Button(
            onClick = { onClick(false) },
            modifier = Modifier
                .weight(hostedWeight)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = hostedColor
            ),
            elevation = ButtonDefaults.buttonElevation(hostedElevation)
        ) {
            Text(text = "Hosted", color = hostedTextColor)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEventsScreenTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = "My Events", fontWeight = FontWeight.Bold) },

        modifier = modifier
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun EventsToShowPreview(modifier: Modifier = Modifier) {
//    UserEventsScreen()
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun EventListItemPreview(modifier: Modifier = Modifier) {
//    EventListItem(
//        eventTitle = "AI Debate: The AGI debate",
//        eventImage = "",
//        eventDate = "Dec 23, 2:00pm",
//        eventLocation = "ISE department",
//        modifier = modifier.padding(16.dp)
//    )
//}