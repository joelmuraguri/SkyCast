package com.joe.settings

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.muraguri.design.widgets.AuthPromptDialog
import com.muraguri.design.widgets.CustomDialog

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
//    val authenticationManager = remember { AuthCredentialManager(context) }
    val scope = rememberCoroutineScope()

   Box(
       contentAlignment = Alignment.Center,
       modifier = Modifier.fillMaxSize()
   ) {
       Text("SETTINGS")
   }
}

//@Composable
//fun SettingsScreen() {
//    val context = LocalContext.current
//    val authenticationManger = remember { AuthCredentialManager(context) }
//    val scope = rememberCoroutineScope()
//
////    LaunchedEffect(Unit) {
////        authenticationManger.signInWithGoogle()
////            .onEach { response ->
////                when (response) {
////                    is AuthResponse.Success -> {
////                        Toast.makeText(context, "Successful Sign In", Toast.LENGTH_SHORT).show()
////                        authenticationManger.saveUserToFirestore()
////                    }
////                    is AuthResponse.Error -> {
////                        Toast.makeText(context, "Try Again. Unsuccessful Sign In: ${response.message}", Toast.LENGTH_SHORT).show()
////                        Log.e("SettingsScreen", "Sign-in error: ${response.message}")
////                    }
////                }
////            }
////            .launchIn(scope)
////    }
//
//    WeatherApp(
//        signIn = {
//            scope.launch {
//                authenticationManger.signInWithGoogle()
//                    .collect { response ->
//                        when (response) {
//                            is AuthResponse.Success -> {
//                                Toast.makeText(context, "Successful Sign In", Toast.LENGTH_SHORT).show()
//                                authenticationManger.saveUserToFirestore()
//                            }
//                            is AuthResponse.Error -> {
//                                val errorMessage = if (response.message.contains("user_cancelled")) {
//                                    "Sign-in cancelled by user."
//                                } else {
//                                    "Try Again. Unsuccessful Sign In: ${response.message}"
//                                }
//                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                                Log.e("SettingsScreen", "Sign-in error: ${response.message}")
//                            }
//                        }
////                        when (response) {
////                            is AuthResponse.Success -> {
////                                Toast.makeText(context, "Successful Sign In", Toast.LENGTH_SHORT).show()
////                                authenticationManger.saveUserToFirestore()
////                            }
////                            is AuthResponse.Error -> {
////                                Toast.makeText(context, "Try Again. Unsuccessful Sign In: ${response.message}", Toast.LENGTH_SHORT).show()
////                                Log.e("SettingsScreen", "Sign-in error: ${response.message}")
////                            }
////                        }
//                    }
//            }
//        }
//    )
//}

@Composable
fun WeatherApp(
    signIn : () -> Unit
) {
    var showAuthDialog by remember { mutableStateOf(false) }

//    if (showAuthDialog){
//        Log.d("AUTH DIALOG", "--------------------> $showAuthDialog")
//        GoogleOauthDialog(
//            onSignInClick = {
//                signIn()
//            },
//            onDismissRequest = {
//                showAuthDialog = false
//            },
//            showDialog = true
//        )
//    }

    if (showAuthDialog) {
        CustomDialog(
            showDialog = showAuthDialog,
            onDismissRequest = {
                showAuthDialog = false
            }
        ) {
            AuthPromptDialog(
                onDismissRequest = { showAuthDialog = false },
                onSignInClick = {
                    // Trigger Google Sign-In flow
                    signIn()
                }
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Button(
            onClick = {
                showAuthDialog = true
                Log.d("AUTH DIALOG", "--------------------> $showAuthDialog")
            }
        ) {
            Text("Add Favorite")
        }
    }
}