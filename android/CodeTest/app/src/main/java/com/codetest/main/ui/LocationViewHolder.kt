package com.codetest.main.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codetest.R
import com.codetest.main.model.WeatherInfo
import kotlinx.android.synthetic.main.location.view.*


class LocationViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): LocationViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.location, parent, false)
            return LocationViewHolder(view)
        }
    }

    fun setup(list: WeatherInfo) {
        itemView.card.setCardBackgroundColor(list.cardColor ?: itemView.resources.getColor(R.color.white))
        itemView.title.text = list.cityName
        itemView.imageView.text = list.temperature
    }
}
