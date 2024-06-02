package com.example.eventtracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.AsyncImage
import com.example.eventtracker.R
import com.example.eventtracker.model.EventData
import com.example.eventtracker.ui.theme.EventTrackerTheme

@Composable
fun EventDetailScreen(modifier: Modifier = Modifier, event: EventData) {
    Scaffold(topBar = { EventDetailScreenTopBar() }) {
        EventDetailScreenContent(modifier = Modifier.padding(it), event = event)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreenTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Event Detail",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Event Detail",
            )
        },
        modifier = modifier
    )

}

@Composable
fun EventDetailScreenContent(modifier: Modifier = Modifier, event: EventData) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(maxWidth)
            ) {
                AsyncImage(
                    model = event.image,
                    error = painterResource(id = R.drawable.default_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = event.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${event.date}, ${event.time}",
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Event Location",
                    tint = Color.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(211, 211, 211, 130))
                        .padding(4.dp)

                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = event.location, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color(211, 211, 211, 130))
                ) {
                    Text(text = "Save", color = Color.Black, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color(13, 125, 242))
                ) {
                    Text(text = "Attend", fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About the Event",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventDetailScreenPreview(modifier: Modifier = Modifier) {
    EventTrackerTheme {
        EventDetailScreen(event = EventData())
    }
}



