package com.codetest.main.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.codetest.main.model.Location
import com.codetest.main.model.LocationsApiState
import com.codetest.main.model.Status
import com.codetest.main.repositories.LocationsRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class LocationsViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var locationsRepository: LocationsRepository

    @Mock
    private lateinit var mockObserver: Observer<LocationsApiState>

    @Captor
    private lateinit var captor: ArgumentCaptor<LocationsApiState>
    private lateinit var viewModel: LocationsViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = LocationsViewModel(locationsRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `when retrieving locations list is returned`() {
        runBlocking {
            val location = Location(
                id = "id",
                name = "name",
                temperature = "temp",
                status = Status.CLOUDY
            )
            val flow = flow {
                emit(LocationsApiState.Result(listOf(location)))
            }
            `when`(locationsRepository.getWeatherLocations()).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.retrieveLocations()
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.Result)
            assertEquals(location, (captor.value as LocationsApiState.Result).result[0])
            assertEquals(1, (captor.value as LocationsApiState.Result).result.size)
        }
    }

    @Test
    fun `when adding a location locations list is returned`() {
        runBlocking {
            val location = Location(
                id = "id",
                name = "name",
                temperature = "temp",
                status = Status.CLOUDY
            )
            val flow = flow {
                emit(LocationsApiState.Result(listOf(location)))
            }
            `when`(locationsRepository.addWeatherLocation(location)).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.addLocation(location)
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.Result)
            assertEquals(location, (captor.value as LocationsApiState.Result).result[0])
            assertEquals(1, (captor.value as LocationsApiState.Result).result.size)
        }
    }

    @Test
    fun `when removing a location it is returned`() {
        runBlocking {
            val location = Location(
                id = "id",
                name = "name",
                temperature = "temp",
                status = Status.CLOUDY
            )
            val flow = flow {
                emit(LocationsApiState.RemoveSuccess(location))
            }
            `when`(locationsRepository.addWeatherLocation(location)).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.addLocation(location)
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.RemoveSuccess)
            assertEquals(location, (captor.value as LocationsApiState.RemoveSuccess).location)
        }
    }

    @Test
    fun `when service emits loading state live data receives it`() {
        runBlocking {
            val flow = flow {
                emit(LocationsApiState.Loading)
            }
            `when`(locationsRepository.getWeatherLocations()).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.retrieveLocations()
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.Loading)
        }
    }

    @Test
    fun `when service emits error state live data receives it`() {
        runBlocking {
            val flow = flow {
                emit(LocationsApiState.Error)
            }
            `when`(locationsRepository.getWeatherLocations()).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.retrieveLocations()
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.Error)
        }
    }

    @Test
    fun `when service emits adding error state live data receives it`() {
        runBlocking {
            val location = Location(
                id = "id",
                name = "name",
                temperature = "temp",
                status = Status.CLOUDY
            )
            val flow = flow {
                emit(LocationsApiState.AddError(location))
            }
            `when`(locationsRepository.getWeatherLocations()).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.retrieveLocations()
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.AddError)
            assertEquals(location, (captor.value as LocationsApiState.AddError).location)
        }
    }

    @Test
    fun `when service emits removing error state live data receives it`() {
        runBlocking {
            val location = Location(
                id = "id",
                name = "name",
                temperature = "temp",
                status = Status.CLOUDY
            )
            val flow = flow {
                emit(LocationsApiState.RemoveError(location))
            }
            `when`(locationsRepository.getWeatherLocations()).thenReturn(flow)
            viewModel.locations.observeForever(mockObserver)
            viewModel.retrieveLocations()
            verify(mockObserver).onChanged(captor.capture())
            assertTrue(captor.value is LocationsApiState.RemoveError)
            assertEquals(location, (captor.value as LocationsApiState.RemoveError).location)
        }
    }
}
