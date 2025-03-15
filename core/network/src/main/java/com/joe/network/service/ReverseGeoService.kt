package com.joe.network.service

import com.joe.network.models.ReverseGeoCodingResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ReverseGeoService {

    @GET("reverse")
    suspend fun getLocationAddress(
        @Query("lat") latitude : Double,
        @Query("lon") longitude : Double,
        @Query("api_key") apiKey : String = "66ed3e1a48139365675555wom5c1f33",
    ) : ApiResponse<ReverseGeoCodingResponse>

}