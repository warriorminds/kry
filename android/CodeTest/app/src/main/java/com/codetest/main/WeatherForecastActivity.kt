package com.codetest.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.codetest.R
import com.codetest.databinding.ActivityMainBinding
import com.codetest.main.adapters.LocationAdapter
import com.codetest.main.model.Location
import com.codetest.main.model.LocationsApiState
import com.codetest.main.model.Status
import com.codetest.main.viewmodels.LocationsViewModel
import com.codetest.main.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import javax.inject.Inject

class WeatherForecastActivity : AppCompatActivity(), LocationAdapter.DeleteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var locationsAdapter: LocationAdapter

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LocationsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        viewModel.locations.observe(this, Observer {
            when (it) {
                is LocationsApiState.Error -> {
                    binding.progress.visibility = View.GONE
                    showError()
                }
                is LocationsApiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
                is LocationsApiState.Result -> {
                    binding.progress.visibility = View.GONE
                    locationsAdapter.addLocations(it.result)
                }
                is LocationsApiState.AddError -> {
                    binding.progress.visibility = View.GONE
                    showAddError(it.location)
                }
                is LocationsApiState.RemoveSuccess -> {
                    binding.progress.visibility = View.GONE
                    locationsAdapter.deleteLocation(it.location)
                    Snackbar.make(binding.recyclerView, getString(R.string.remove_success), Snackbar.LENGTH_SHORT)
                        .show()
                }
                is LocationsApiState.RemoveError -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.recyclerView, "Error removing location", Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry) { view ->
                            viewModel.removeLocation(it.location)
                        }
                        .show()
                }
            }
        })
        viewModel.retrieveLocations()
    }

    private fun init() {
        locationsAdapter.addListener(this)
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@WeatherForecastActivity, LinearLayoutManager.VERTICAL, false)
            adapter = locationsAdapter
        }

        binding.fabAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showError() {
        Snackbar.make(binding.recyclerView, getString(R.string.error_loading_locations), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                viewModel.retrieveLocations()
            }.show()
    }

    private fun showAddError(location: Location) {
        Snackbar.make(binding.recyclerView, getString(R.string.error_adding_location), Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.addLocation(location)
            }
            .show()
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.add_location_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.add_location))
            .setView(view)
            .setPositiveButton(
                getString(R.string.add)
            ) { _, _ ->
                if (!addLocation(view)) {
                    Snackbar.make(binding.recyclerView, getString(R.string.empty_error), Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val spinner = view.findViewById<Spinner>(R.id.status)
                spinner.adapter = adapter
            }
        dialog.show()
    }

    private fun addLocation(view: View): Boolean {
        val locationName = view.findViewById<EditText>(R.id.location_name_edit_text).text.toString()
        val temperature = view.findViewById<EditText>(R.id.temperature_edit_text).text.toString()
        val selectedStatusPosition = view.findViewById<Spinner>(R.id.status).selectedItemPosition

        if (locationName.isEmpty() || temperature.isEmpty()) {
            return false
        }
        viewModel.addLocation(
            Location(
                id = null,
                name = locationName,
                temperature = temperature,
                status = Status.from(selectedStatusPosition)
            )
        )
        return true
    }

    override fun delete(location: Location) {
        viewModel.removeLocation(location)
    }
}