package com.joe.models

import android.net.Uri

data class User(
    val userId : String ?= "",
    val name : String ?= "",
    val profileUrl : Uri?= null,
    val email : String ?= ""
)


// Data Models
data class SignInState(val isSignInSuccessful: Boolean = false, val signInError: String? = null)
data class SignInResult(val data: UserData?, val errorMessage: String?)
data class UserData(val uid: String, val username: String?, val profilePictureUrl: String?)
