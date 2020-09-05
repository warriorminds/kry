package com.codetest.main

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.codetest.WeatherApplication
import java.util.*

class KeyUtil {

    companion object {
        const val KEY = "api_key"
    }

    private fun preferences(): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(WeatherApplication.appContext())

    fun getKey(): String {
        preferences().getString(KEY, null)?.let {
            return it
        } ?: kotlin.run {
            val apiKey = UUID.randomUUID().toString()
            preferences()
                .edit()
                .putString(KEY, apiKey)
                .apply()
            return apiKey
        }
    }
}