package com.codetest.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.codetest.R
import com.codetest.main.model.WeatherInfo
import com.codetest.main.ui.AddLocationDialogFragment
import com.codetest.main.ui.LocationViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class WeatherForecastActivity : AppCompatActivity(), View.OnClickListener, AddLocationDialogFragment.Listener {

    private var adapter = ListAdapter()
    private val list: ArrayList<WeatherInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        actionButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        AddLocationDialogFragment.show(this.supportFragmentManager)
    }

    override fun onSimpleDialogResult(value: String) {
        // TODO  post city name to add in the list

    }


    private inner class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LocationViewHolder.create(parent)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            (viewHolder as? LocationViewHolder)?.setup(list[position])
        }
    }
}