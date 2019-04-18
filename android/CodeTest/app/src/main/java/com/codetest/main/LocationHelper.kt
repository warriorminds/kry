package com.codetest.main

import com.codetest.main.api.LocationApiService
import com.codetest.main.model.Location
import com.codetest.main.model.Status
import java.util.*

class LocationHelper {

    companion object {
        fun getLocations(callback: (List<Location>?) -> Unit) {
            val locations: ArrayList<Location> = arrayListOf()
            val apiKey = KeyUtil().getKey()
            LocationApiService.getApi().get(apiKey, "locations", {
                val list = it.get("locations").asJsonArray
                for (json in list) {
                    locations.add(Location.from(json.asJsonObject))
                }
                callback(locations)
            }, {
                callback(null)
            })
        }

        fun addLocation(name: String, callback: (Boolean) -> Unit) {
            val apiKey = KeyUtil().getKey()

            val location = Location(
                UUID.randomUUID().toString(),
                name,
                (-10..45).shuffled().first().toString(),
                Status.values().toList().shuffled().first()
            )

            LocationApiService.getApi().post(apiKey, "locations", location, {
                callback(true)
            }, {
                callback(false)
            })
        }
    }

}