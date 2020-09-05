package com.codetest.main.model

sealed class LocationsApiState {
    object Loading : LocationsApiState()
    object Error : LocationsApiState()
    data class RemoveSuccess(val location: Location) : LocationsApiState()
    data class AddError(val location: Location) : LocationsApiState()
    data class RemoveError(val location: Location) : LocationsApiState()
    data class Result(val result: List<Location>) : LocationsApiState()
}