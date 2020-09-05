package com.codetest.main.repositories

import com.codetest.main.model.Location
import com.codetest.main.model.LocationsApiState
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {
    suspend fun getWeatherLocations(): Flow<LocationsApiState>

    suspend fun addWeatherLocation(location: Location): Flow<LocationsApiState>

    suspend fun removeLocation(location: Location): Flow<LocationsApiState>
}