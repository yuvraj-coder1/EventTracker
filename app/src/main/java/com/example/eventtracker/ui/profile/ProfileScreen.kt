package com.example.eventtracker.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.eventtracker.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, name: String, email: String = "") {
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
        Text(text = name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "USN - 1DS23IS188",
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
        EventList()
    }
}

@Composable
fun EventList(modifier: Modifier = Modifier) {
    EventItem(eventName = "Introduction to Android Development")
    EventItem(eventName = "Introduction to Android Development")
    EventItem(eventName = "Introduction to Android Development")
    EventItem(eventName = "Introduction to Android Development")
    EventItem(eventName = "Introduction to Android Development")

}

@Composable
fun EventItem(modifier: Modifier = Modifier,eventName:String) {
    Row(modifier = modifier.padding(vertical = 16.dp).clickable { /*TODO*/ }) {
        AsyncImage(
            model = null,
            contentDescription = "Event image",
            modifier = modifier
                .clip(MaterialTheme.shapes.medium)
                .height(70.dp)
                .width(100.dp)
            ,
            error = painterResource(id = R.drawable.default_image),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = eventName , overflow = TextOverflow.Ellipsis,maxLines = 2, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = { Text(text = "Profile", fontWeight = FontWeight.Bold) })
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(name = "James")
}