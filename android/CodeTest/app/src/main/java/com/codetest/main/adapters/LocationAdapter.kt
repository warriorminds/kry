package com.codetest.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codetest.R
import com.codetest.databinding.LocationItemBinding
import com.codetest.main.model.Location
import com.codetest.main.model.Status
import javax.inject.Inject

class LocationAdapter @Inject constructor() : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    private val locations = mutableListOf<Location>()
    private var listener: DeleteListener? = null

    class ViewHolder(private val binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location, position: Int, listener: DeleteListener?) {
            with(binding) {
                card.setCardBackgroundColor(getColor(location.status))
                locationName.text = location.name
                weatherInfo.text = "${location.temperature} °C ${String(Character.toChars(location.status.value))}"
                delete.setOnClickListener {
                    location.id?.let {
                        listener?.delete(location)
                    }
                }
            }
        }

        private fun getColor(status: Status): Int {
            return when (status) {
                Status.SUNNY, Status.MOSTLY_SUNNY, Status.PARTLY_SUNNY, Status.PARTLY_SUNNY_RAIN, Status.BARELY_SUNNY
                -> itemView.resources.getColor(R.color.blue)
                Status.THUNDER_CLOUD_AND_RAIN, Status.TORNADO, Status.LIGHTENING -> itemView.resources.getColor(R.color.red)
                Status.CLOUDY, Status.SNOW_CLOUD, Status.RAINY -> itemView.resources.getColor(R.color.grey)
            }
        }
    }

    fun addListener(deleteListener: DeleteListener) {
        this.listener = deleteListener
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position], position, listener)
    }

    fun addLocations(locations: List<Location>) {
        val diffCallback = LocationsDiffCallback(this.locations, locations)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.locations.addAll(locations)
        diffResult.dispatchUpdatesTo(this)
    }

    fun deleteLocation(location: Location) {
        val position = locations.indexOf(location)
        this.locations.remove(location)
        notifyItemRemoved(position)
    }

    interface DeleteListener {
        fun delete(location: Location)
    }
}