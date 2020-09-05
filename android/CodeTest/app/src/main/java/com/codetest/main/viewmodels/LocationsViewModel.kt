package com.codetest.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetest.main.model.Location
import com.codetest.main.model.LocationsApiState
import com.codetest.main.repositories.LocationsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository
) : ViewModel() {

    var locations: MutableLiveData<LocationsApiState> = MutableLiveData()

    fun retrieveLocations() {
        locations.value = LocationsApiState.Loading
        viewModelScope.launch {
            locationsRepository.getWeatherLocations().collect {
                locations.value = it
            }
        }
    }

    fun addLocation(location: Location) {
        locations.value = LocationsApiState.Loading
        viewModelScope.launch {
            locationsRepository.addWeatherLocation(location).collect {
                locations.value = it
            }
        }
    }

    fun removeLocation(location: Location) {
        locations.value = LocationsApiState.Loading
        viewModelScope.launch {
            locationsRepository.removeLocation(location).collect {
                locations.value = it
            }
        }
    }
}