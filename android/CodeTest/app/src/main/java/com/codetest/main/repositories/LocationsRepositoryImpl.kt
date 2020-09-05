package com.codetest.main.repositories

import com.codetest.main.api.LocationApiService
import com.codetest.main.model.Location
import com.codetest.main.model.LocationsApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationsRepositoryImpl @Inject constructor(
    private val weatherService: LocationApiService
) : LocationsRepository {
    override suspend fun getWeatherLocations() = flow {
        try {
            val response = weatherService.getLocations()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    emit(LocationsApiState.Result(response.body()!!.locations))
                } else {
                    emit(LocationsApiState.Result(listOf()))
                }
            } else {
                emit(LocationsApiState.Error)
            }
        } catch (e: Exception) {
            emit(LocationsApiState.Error)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addWeatherLocation(location: Location) = flow {
        try {
            val response = weatherService.addLocation(location)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val locations = listOf(response.body()!!)
                    emit(LocationsApiState.Result(locations))
                } else {
                    emit(LocationsApiState.AddError(location))
                }
            } else {
                emit(LocationsApiState.AddError(location))
            }
        } catch (e: Exception) {
            emit(LocationsApiState.AddError(location))
        }
    }
        .flowOn(Dispatchers.IO)

    override suspend fun removeLocation(location: Location) = flow {
        try {
            val response = weatherService.removeLocation(location.id!!)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    emit(LocationsApiState.RemoveSuccess(location))
                } else {
                    emit(LocationsApiState.RemoveError(location))
                }
            } else {
                if (response.code() in 400..499) {
                    emit(LocationsApiState.RemoveSuccess(location))
                } else {
                    emit(LocationsApiState.RemoveError(location))
                }
            }
        } catch (e: Exception) {
            emit(LocationsApiState.RemoveError(location))
        }
    }.flowOn(Dispatchers.IO)
}