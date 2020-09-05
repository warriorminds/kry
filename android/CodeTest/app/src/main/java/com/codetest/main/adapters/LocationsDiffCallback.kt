package com.codetest.main.adapters

import androidx.recyclerview.widget.DiffUtil
import com.codetest.main.model.Location

class LocationsDiffCallback(
    private val oldList: List<Location>,
    private val newList: List<Location>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id &&
            oldList[oldItemPosition].name == newList[newItemPosition].name &&
            oldList[oldItemPosition].status == newList[newItemPosition].status &&
            oldList[oldItemPosition].temperature == newList[newItemPosition].temperature
}