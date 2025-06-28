package com.example.eventtracker.ui.signIn

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RememberMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventtracker.R
import com.example.eventtracker.ui.theme.EventTrackerTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(),
    navigateToHome: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    navigateToSignIn: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
//        if(viewModel.checkIfLoggedIn()){
//            navigateToHome()
//            Log.d("TAG", "SignInScreen: ${viewModel.checkIfLoggedIn()}")
//        }
    }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    SignInScreenContent(
        viewModel = viewModel,
        modifier = Modifier.padding(16.dp),
        onSignIn =
        {
            if (uiState.username.isEmpty() || uiState.password.isEmpty()) {
                Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.signIn(
                    onSuccess = {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    },
                    onFailure = {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                )
            }

        },
        onSignUp = {
            if (uiState.email.isEmpty() ||
                uiState.password.isEmpty() ||
                uiState.username.isEmpty() ||
                uiState.collegeId.isEmpty() ||
                uiState.confirmPassword.isEmpty()
            )
                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show()
            else
                viewModel.signUp(
                    onSuccess = {
                        Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                    }
                )
        },
        uiState = uiState
    )
}

@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
    onSignIn: () -> Unit = {},
    onSignUp: () -> Unit = {},
    uiState: SignInUiState,
) {
    var hidePassWord by rememberSaveable {
        mutableStateOf(true)
    }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(text = "Log In", style = TextStyle(fontSize = 30.sp), fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.size(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Button(
                onClick = { viewModel.updateIsSignIn(true) },
                colors = if (uiState.isSignIn) ButtonDefaults.buttonColors(Color.Black) else ButtonDefaults.buttonColors(
                    Color.LightGray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Sign in", color = if (uiState.isSignIn) Color.White else Color.Black)
//                if (isLawyer) {
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Image(
//                        painter = painterResource(id = R.drawable.lawyer_icon),
//                        contentDescription = "lawyer image",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
            }
            Button(
                onClick = { viewModel.updateIsSignIn(false) },
                colors = if (uiState.isSignIn) ButtonDefaults.buttonColors(Color.LightGray) else ButtonDefaults.buttonColors(
                    Color.Black
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Sign up", color = if (!uiState.isSignIn) Color.White else Color.Black)
//                if (!isLawyer) {
//                    Spacer(modifier = Modifier.weight(1f))
//                    Image(
//                        painter = painterResource(id = R.drawable.client_icon),
//                        contentDescription = "client image",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        if (!uiState.isSignIn) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { viewModel.updateName(it) },
                label = { Text(text = "Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Username"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = uiState.collegeId,
                onValueChange = { viewModel.updateCollegeId(it) },
                label = { Text(text = "College Id") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.RememberMe,
                        contentDescription = "College Id"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { viewModel.updateName(it) },
            label = { Text(text = "Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "username"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text(text = "Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Key,
                    contentDescription = "Password"
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (hidePassWord) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                Icon(
                    imageVector = if (!hidePassWord) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Hide Password",
                    modifier = Modifier.clickable { hidePassWord = !hidePassWord })
            }
        )
        if (!uiState.isSignIn) {
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = { Text(text = "Confirm Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Key,
                        contentDescription = "Password"
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (hidePassWord) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    Icon(
                        imageVector = if (!hidePassWord) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Hide Password",
                        modifier = Modifier.clickable { hidePassWord = !hidePassWord })
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (uiState.isSignIn) onSignIn() else onSignUp()
                focusManager.clearFocus()
            },
            colors = ButtonDefaults.buttonColors(
                Color.Black
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (uiState.isSignIn) "Log in" else "Sign Up",
                fontSize = 16.sp,
                modifier = Modifier.padding(4.dp)
            )
        }

//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = { /*TODO*/ },
//            colors = ButtonDefaults.buttonColors(
//                Color.Black
//            ),
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.google),
//                contentDescription = "Google",
//                modifier = Modifier
//                    .size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Text(
//                text = stringResource(R.string.log_in_with_google),
//                fontSize = 16.sp,
//                modifier = Modifier
//                    .padding(4.dp)
//            )
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = stringResource(R.string.don_t_have_an_account),
//            modifier = Modifier.align(alignment = Alignment.Start)
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Button(
//            onClick = onSignUpClicked,
//            colors = ButtonDefaults.buttonColors(
//                Color.Black
//            ),
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Text(text = stringResource(R.string.sign_up), fontSize = 16.sp, modifier = Modifier.padding(4.dp))
//        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenContentPreview(modifier: Modifier = Modifier) {
    EventTrackerTheme {
        SignInScreen()
    }
}