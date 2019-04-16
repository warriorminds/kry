package com.codetest.main.model

import android.graphics.Color
import com.google.gson.JsonObject

enum class Emoji(val value: Int) {
    CLOUDY(0x2601),
    SUNNY(0x2600),
    MOSTLY_SUNNY(0x1F324),
    PARTLY_SUNNY_RAIN(0x1F326),
    THUNDER_CLOUD_AND_RAIN(0x26C8),
    TORNADO(0x1F32A),
    BARELY_SUNNY(0x1F325),
    LIGHTENING(0x1F329),
    SNOW_CLOUD(0x1F328),
    RAIN_CLOUD(0x1F327),
    SNOW_MAN(0x26C4);
}

class WeatherInfo(
    val cardColor: Int?,
    val cityName: String?,
    val temperature: String?
) {

    companion object {
        fun from(jsonObject: JsonObject): WeatherInfo {
            return WeatherInfo(
                Color.parseColor(jsonObject.get("card_color").asString),
                jsonObject.get("city_name").asString,
                getTemperature(jsonObject)
            )
        }

        private fun getTemperature(jsonObject: JsonObject): String {
            val temperature = jsonObject.get("temperature").asString
            val emojiUnicode = getEmojiUnicode(jsonObject.get("emoji").asString)
            return temperature + String(Character.toChars(emojiUnicode))
        }

        fun getEmojiUnicode(value: String): Int {
            return when (value) {
                "cloudy" -> Emoji.CLOUDY.value
                "sunny" -> Emoji.SUNNY.value
                "mostly_sunny" -> Emoji.MOSTLY_SUNNY.value
                "partly_sunny_rain" -> Emoji.PARTLY_SUNNY_RAIN.value
                "thunder_cloud_and_rain" -> Emoji.THUNDER_CLOUD_AND_RAIN.value
                "tornado" -> Emoji.TORNADO.value
                "barely_sunny" -> Emoji.BARELY_SUNNY.value
                "lightening" -> Emoji.LIGHTENING.value
                "snow_cloud" -> Emoji.SNOW_CLOUD.value
                "rainy" -> Emoji.RAIN_CLOUD.value
                else -> Emoji.SNOW_MAN.value
            }
        }
    }
}