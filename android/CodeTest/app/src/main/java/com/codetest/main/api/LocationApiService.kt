package com.codetest.main.api

import com.codetest.main.model.Location
import com.codetest.main.model.LocationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface LocationApiService {
    @GET("/locations")
    suspend fun getLocations(): Response<LocationResponse>

    @POST("/locations")
    suspend fun addLocation(@Body location: Location): Response<Location>

    @DELETE("/locations/{id}")
    suspend fun removeLocation(@Path("id") id: String): Response<Unit>
}
