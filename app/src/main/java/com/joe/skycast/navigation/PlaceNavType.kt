package com.joe.skycast.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.joe.models.Location
import com.joe.models.Place
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object PlaceNavType {

    val placeType = object : NavType<Place>(
        isNullableAllowed = true
    ){
        override fun get(bundle: Bundle, key: String): Place? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Place {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: Place) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: Place): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }

    val locationType = object : NavType<Location>(
        isNullableAllowed = true
    ){
        override fun get(bundle: Bundle, key: String): Location? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Location {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: Location) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: Location): String {
            return Uri.encode(Json.encodeToString(value))
        }

    }
}