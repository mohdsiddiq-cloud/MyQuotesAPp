package com.example.android.myquotes

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): List<String>? {
        return value?.let { Gson().fromJson(it, object : TypeToken<List<String>>() {}.type) }
    }

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }
}