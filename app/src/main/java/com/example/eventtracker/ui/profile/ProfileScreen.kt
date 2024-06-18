package com.example.eventtracker.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.eventtracker.R
import com.example.eventtracker.model.EventData

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,navigateToLogin: () -> Unit) {
    val viewModel: ProfileScreenViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = null,
            contentDescription = "Profile picture",
            modifier = modifier
                .clip(CircleShape)
                .size(150.dp),
            error = painterResource(id = R.drawable.default_profile_image),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = viewModel.userName.collectAsState().value,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "USN - ${viewModel.userUsn.collectAsState().value}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Branch - ISE",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Section - E",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Events",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        EventList(events = viewModel.userEvents.collectAsState().value)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
//                    viewModel.logOut()
                    navigateToLogin()
                          },
                modifier = Modifier.fillMaxWidth(0.87f),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                    Text(text = "Logout")
                }

        }
    }
}

@Composable
fun EventList(modifier: Modifier = Modifier, events: List<EventData>) {
    Column() {
        events.forEachIndexed { index, event ->
            EventItem(event = event)
        }
    }
//    EventItem(eventName = "Introduction to Android Development")
//    EventItem(eventName = "Introduction to Android Development")
//    EventItem(eventName = "Introduction to Android Development")
//    EventItem(eventName = "Introduction to Android Development")
//    EventItem(eventName = "Introduction to Android Development")

}

@Composable
fun EventItem(modifier: Modifier = Modifier, event: EventData) {
    Row(modifier = modifier
        .padding(vertical = 16.dp)
        .clickable { /*TODO*/ }) {
        AsyncImage(
            model = event.image,
            contentDescription = "Event image",
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .height(70.dp)
                .width(100.dp),
            error = painterResource(id = R.drawable.default_image),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = event.name, overflow = TextOverflow.Ellipsis, maxLines = 2, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = { Text(text = "Profile", fontWeight = FontWeight.Bold) })
}

//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen(name = "James")
//}