package com.codetest.main.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.EditText
import com.codetest.R


class AddLocationDialogFragment : DialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager) {
            val dialog = AddLocationDialogFragment()
            dialog.show(fragmentManager, "ADD")
        }
    }

    interface Listener {
        fun onSimpleDialogResult(value: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity?.baseContext).inflate(R.layout.dialog_add_location, null)

        val builder = AlertDialog.Builder(activity)
            .setView(inflater)
            .setTitle("Add City")
            .setPositiveButton("OK") { dialog, index ->
                val cityName = inflater.findViewById(R.id.city_name) as? EditText
                (activity as? Listener)?.onSimpleDialogResult(cityName?.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { _, _ -> dialog.dismiss() }

        isCancelable = false
        return builder.create()
    }
}