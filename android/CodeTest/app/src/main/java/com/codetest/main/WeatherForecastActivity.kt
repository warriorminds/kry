package com.codetest.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.codetest.R
import com.codetest.main.model.WeatherInfo
import com.codetest.main.ui.AddDialogFragment
import com.codetest.main.ui.CitiesListViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class WeatherForecastActivity : AppCompatActivity(), View.OnClickListener, AddDialogFragment.Listener {

    private var adapter = ListAdapter()
    private val list: ArrayList<WeatherInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.add(
            WeatherInfo(
                resources.getColor(R.color.blue),
                "Paris",
                "15°C " + String(Character.toChars(WeatherInfo.getEmojiUnicode("sunny")))
            )
        )
        list.add(
            WeatherInfo(
                resources.getColor(R.color.grey),
                "Stockholm",
                "5°C " + String(Character.toChars(WeatherInfo.getEmojiUnicode("rainy")))
            )
        )
        list.add(
            WeatherInfo(
                resources.getColor(R.color.blue),
                "Paris",
                "15°C " + String(Character.toChars(WeatherInfo.getEmojiUnicode("blurry")))
            )
        )
        list.add(
            WeatherInfo(
                resources.getColor(R.color.grey),
                "Stockholm",
                "5°C " + String(Character.toChars(WeatherInfo.getEmojiUnicode("tornado")))
            )
        )

        adapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        actionButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        AddDialogFragment.show(this.supportFragmentManager)
    }

    override fun onSimpleDialogResult(value: String) {
        // TODO  post city name to add in the list

        list.add(
            WeatherInfo(
                resources.getColor(R.color.grey),
                value,
                "5°C " + String(Character.toChars(WeatherInfo.getEmojiUnicode("rainy")))
            )
        )

        adapter.notifyDataSetChanged()
    }


    private inner class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CitiesListViewHolder.create(parent)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            (viewHolder as? CitiesListViewHolder)?.setup(list[position])
        }
    }
}