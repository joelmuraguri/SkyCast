package com.joe.models

data class User(
    val uid : String,
    val name : String,
    val email : String,
    val profileUrl : String? = "",
)


// Data Models
data class SignInState(val isSignInSuccessful: Boolean = false, val signInError: String? = null)
data class SignInResult(val data: UserData?, val errorMessage: String?)
data class UserData(val uid: String, val username: String?, val profilePictureUrl: String?)
