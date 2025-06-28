package com.example.eventtracker.ui.postNewEvent

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.eventtracker.R
import com.example.eventtracker.ui.theme.EventTrackerTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date

@Composable
fun PostNewEventScreen(modifier: Modifier = Modifier, getEvents: () -> Unit) {
    val viewModel: PostNewEventViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    PostNewEventBody(
        modifier = Modifier.padding(),
        uiState = uiState,
        viewModel = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostNewEventScreenTopBar(modifier: Modifier = Modifier, onClickAction: () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = { Text("New Event") },
        navigationIcon = {
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = "Close",
//                modifier = Modifier.clickable { onClickAction() })
        }
    )
}

@Composable
fun PostNewEventBody(
    modifier: Modifier = Modifier,
    viewModel: PostNewEventViewModel = viewModel(),
    uiState: PostNewEventUiState,
) {

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    viewModel.uri = selectedImageUri
    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = uiState.eventName,
            onValueChange = { viewModel.updateEventName(it) },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Event Name", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(176, 183, 192, 70),
                focusedContainerColor = Color(176, 183, 192, 70),

                ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = uiState.eventDescription,
            onValueChange = { viewModel.updateEventDescription(it) },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Event Description", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(176, 183, 192, 70),
                focusedContainerColor = Color(176, 183, 192, 70),

                ),
            minLines = 7,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = uiState.eventCategory,
            onValueChange = { viewModel.updateEventCategory(it) },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Event Category", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(176, 183, 192, 70),
                focusedContainerColor = Color(176, 183, 192, 70),

                ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = uiState.eventLink,
            onValueChange = { viewModel.updateEventLink(it) },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Event Register Link", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(176, 183, 192, 70),
                focusedContainerColor = Color(176, 183, 192, 70),

                ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        var date by rememberSaveable { mutableStateOf("") }
        date = pickDate()
        viewModel.updateEventDate(date)
        var time by rememberSaveable { mutableStateOf("") }
        time = timePickerDemo()
        val focusManager = LocalFocusManager.current
        viewModel.updateEventTime(time)
        OutlinedTextField(
            value = uiState.location,
            onValueChange = { viewModel.updateLocation(it) },
            shape = RoundedCornerShape(8.dp),
            label = { Text("Location", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Color(176, 183, 192, 70),
                focusedContainerColor = Color(176, 183, 192, 70),

                ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Choose Location"
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            singleImagePickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }) {
            Text(text = "Add Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        AsyncImage(
            model = viewModel.uri,
            contentDescription = "Selected Image",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.addEventToDatabase(onSuccess = {
                    Toast.makeText(
                        context,
                        "Event added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                    onFail = {
                        Toast.makeText(
                            context,
                            "Event not added Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(Color(13, 125, 242))
        ) {
            Text(text = "Create Event", style = MaterialTheme.typography.titleMedium)
        }
    }


}

fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pickDate(): String {

    // Fetching the Local Context
    val mContext = LocalContext.current
    var globalPosition = remember { mutableStateOf(Offset.Zero) }

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            //          mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            // year-month-day
            mDate.value = "$mDayOfMonth ${getMonthName(mMonth + 1)} $mYear"
        }, mYear, mMonth, mDay
    )
// correct code
//    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//
//        // Creating a button that on
//        // click displays/shows the DatePickerDialog
//        Button(onClick = {
//            mDatePickerDialog.show()
//        } ) {
//            Text(text = "Open Date Picker", color = Color.White)
//        }
////        , colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))
//
//        // Adding a space of 100dp height
//        Spacer(modifier = Modifier.size(100.dp))
//
//        // Displaying the mDate value in the Text
//        Text(text = "Selected Date: ${mDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
//    }
//    val selectedDate = remember { mutableStateOf<Date?>(null) }


    // Creating a TextField with an icon
    OutlinedTextField(
        value = if (mDate.value != "") {
            "Selected Date: ${mDate.value}"
        } else {
            ""
        },
        onValueChange = { /* Handle text changes if needed */ },
        label = { Text(text = "Choose Date") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            disabledBorderColor = Color.Transparent,
            disabledContainerColor = Color(176, 183, 192, 70),
            focusedContainerColor = Color(176, 183, 192, 70),
            disabledTrailingIconColor = Color.Black,
            unfocusedPlaceholderColor = Color.Gray,
            disabledTextColor = Color.Black
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),

//                .padding(16.dp),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth, // Replace with your icon resource
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        mDatePickerDialog.show()
                    }
                    .onGloballyPositioned { coordinates ->
                        // Update the absolute position when the button is positioned in the layout hierarchy
                        globalPosition.value = coordinates.positionInWindow()
                        println("globalPosition in root: x:" + globalPosition.value.x + " y : " + globalPosition.value.y)
                    },
            )
        },
        readOnly = true,
        enabled = false
    )

    // Adding a space of 16.dp height
//        Spacer(modifier = Modifier.size(16.dp))
//
//        // Displaying the selected date value in the Text
//        Text(
////            text = "Selected Date: ${selectedDate.value?.toString() ?: ""}",
//            text = "Selected Date: ${mDate.value}",
//            fontSize = 30.sp,
//            textAlign = TextAlign.Center
//        )

    return mDate.value
}

@SuppressLint("DefaultLocale")
@Composable
fun timePickerDemo(): String {
    var time by remember { mutableStateOf("") }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val amPM = if (selectedHour >= 12) "PM" else "AM"
            time = String.format("%02d:%02d $amPM", selectedHour % 13, selectedMinute)
        },
        hour,
        minute,
        false
    )

    OutlinedTextField(
        value = if (time != "") {
            "Selected time: $time"
        } else {
            ""
        },
        onValueChange = { },
        shape = RoundedCornerShape(8.dp),
        label = { Text("Choose Time", color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            disabledBorderColor = Color.Transparent,
            disabledContainerColor = Color(176, 183, 192, 70),
            focusedContainerColor = Color(176, 183, 192, 70),
            disabledTrailingIconColor = Color.Black,
            unfocusedPlaceholderColor = Color.Gray,
            disabledTextColor = Color.Black

        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Choose Time",
                modifier = Modifier.clickable {
                    timePickerDialog.show()
                }
            )
        },
        readOnly = true,
        enabled = false
    )
//
//    Spacer(modifier = Modifier.height(10.dp))
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = time,
//            modifier = Modifier.padding(start = 15.dp),
//            fontWeight = FontWeight.Medium,
//            fontSize = 16.sp,
//            letterSpacing = (-0.3).sp
//        )
//        Button(
//            onClick = { timePickerDialog.show() },
//            shape = RoundedCornerShape(5.dp)
//
//        ) {
//            Text(text = "Pick Time")
//        }
//    }
    return time
}

@Preview
@Composable
fun PostNewEventPreview(modifier: Modifier = Modifier) {
    EventTrackerTheme {
        PostNewEventScreen(getEvents = {})
    }
}