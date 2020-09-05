package com.codetest.main.repositories

import com.codetest.main.api.LocationApiService
import com.codetest.main.model.Location
import com.codetest.main.model.LocationResponse
import com.codetest.main.model.LocationsApiState
import com.codetest.main.model.Status
import com.google.gson.Gson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.File

class LocationRepositoryTest {
    @Mock
    lateinit var service: LocationApiService
    lateinit var repositoryImpl: LocationsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repositoryImpl = LocationsRepositoryImpl(service)
    }

    @Test
    fun `when response is returned from the service then list of locations is returned`() {
        val response =
            Gson().fromJson(getJson("json/locations.json", this.javaClass.classLoader!!), LocationResponse::class.java)
        runBlocking {
            `when`(service.getLocations()).thenReturn(Response.success(200, response))
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.getWeatherLocations().toList(results)

            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.Result)
            assertEquals(5, (results[1] as LocationsApiState.Result).result.size)
        }
    }

    @Test
    fun `when response is error from the service then error is returned`() {
        runBlocking {
            `when`(service.getLocations()).thenReturn(Response.error(404, ResponseBody.create(null, "")))
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.getWeatherLocations().toList(results)
            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.Error)
        }
    }

    @Test
    fun `when location is added, new location is returned`() {
        val location = Location(
            id = "id",
            name = "name",
            temperature = "10",
            status = Status.CLOUDY
        )
        val response =
            Gson().fromJson(getJson("json/location.json", this.javaClass.classLoader!!), Location::class.java)
        runBlocking {
            `when`(service.addLocation(location)).thenReturn(Response.success(200, response))
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.addWeatherLocation(location).toList(results)

            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.Result)
            with(results[1] as LocationsApiState.Result) {
                assertNotNull(result)
                assertEquals(1, result.size)
                assertEquals("83d7164b-9164-4682-a197-0ce54182554a", result[0].id)
                assertEquals("Stockholm", result[0].name)
                assertEquals("RAINY", result[0].status.name)
                assertEquals("9", result[0].temperature)
            }
        }
    }

    @Test
    fun `when response is error when adding location then error is returned`() {
        runBlocking {
            val newLocation = Location(
                id = "id",
                name = "name",
                temperature = "10",
                status = Status.CLOUDY
            )

            `when`(service.addLocation(newLocation)).thenReturn(Response.error(404, ResponseBody.create(null, "")))
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.addWeatherLocation(newLocation).toList(results)

            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.AddError)

            with(results[1] as LocationsApiState.AddError) {
                assertEquals(newLocation, location)
            }
        }
    }

    @Test
    fun `when location is removed, location is returned`() {
        val locationToRemove = Location(
            id = "id",
            name = "name",
            temperature = "10",
            status = Status.CLOUDY
        )
        runBlocking {
            `when`(service.removeLocation(locationToRemove.id!!)).thenReturn(Response.success(200, Unit))
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.removeLocation(locationToRemove).toList(results)
            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.RemoveSuccess)
            with(results[1] as LocationsApiState.RemoveSuccess) {
                assertNotNull(location)
                assertEquals(locationToRemove, location)
            }
        }
    }

    @Test
    fun `when response 4XX then return remove success with location`() {
        runBlocking {
            val removeLocation = Location(
                id = "id",
                name = "name",
                temperature = "10",
                status = Status.CLOUDY
            )

            `when`(service.removeLocation(removeLocation.id!!)).thenReturn(
                Response.error(
                    404,
                    ResponseBody.create(null, "")
                )
            )
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.removeLocation(removeLocation).toList(results)
            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.RemoveSuccess)
            with(results[1] as LocationsApiState.RemoveSuccess) {
                assertEquals(removeLocation, location)
            }
        }
    }

    @Test
    fun `when response 5XX then return remove error with location`() {
        runBlocking {
            val removeLocation = Location(
                id = "id",
                name = "name",
                temperature = "10",
                status = Status.CLOUDY
            )

            `when`(service.removeLocation(removeLocation.id!!)).thenReturn(
                Response.error(
                    500,
                    ResponseBody.create(null, "")
                )
            )
            val results = mutableListOf<LocationsApiState>()
            repositoryImpl.removeLocation(removeLocation).toList(results)
            assertEquals(2, results.size)
            assertTrue(results[0] is LocationsApiState.Loading)
            assertTrue(results[1] is LocationsApiState.RemoveError)
            with(results[1] as LocationsApiState.RemoveError) {
                assertEquals(removeLocation, location)
            }
        }
    }

    private fun getJson(jsonFile: String, classLoader: ClassLoader): String {
        val uri = classLoader.getResource(jsonFile)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}