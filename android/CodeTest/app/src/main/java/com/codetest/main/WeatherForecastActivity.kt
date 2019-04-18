package com.codetest.main

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.codetest.main.model.Location
import com.codetest.main.ui.AddLocationDialogFragment
import com.codetest.main.ui.LocationViewHolder
import kotlinx.android.synthetic.main.activity_main.*


class WeatherForecastActivity : AppCompatActivity(), View.OnClickListener, AddLocationDialogFragment.Listener,
    SwipeRefreshLayout.OnRefreshListener {

    private var adapter = ListAdapter()
    private var list: List<Location> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.codetest.R.layout.activity_main)

        swipeLayout?.setOnRefreshListener(this)
        swipeLayout?.isEnabled = true

        adapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        actionButton.setOnClickListener(this)
        fetchLocations()
    }

    override fun onRefresh() {
        fetchLocations()
    }

    override fun onClick(v: View?) {
        AddLocationDialogFragment.show(this.supportFragmentManager)
    }

    override fun onSimpleDialogResult(value: String) {
        LocationHelper.addLocation(value) {
            if (it) {
                fetchLocations()

            } else {
                showError()

            }
        }
    }

    private fun fetchLocations() {
        LocationHelper.getLocations { response ->
            if (response == null) {
                showError()
            } else {
                if (swipeLayout.isRefreshing) {
                    swipeLayout.isRefreshing = false
                }

                list = response
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showError() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Something went wrong")
            .setPositiveButton("Ok", { _, _ -> })
            .create()
            .show()
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