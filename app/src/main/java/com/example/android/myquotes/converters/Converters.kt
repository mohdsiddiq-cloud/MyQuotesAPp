package com.example.android.myquotes.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
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

    @TypeConverters
    @JvmStatic
    fun fromList1(list: List<String>?): String? {
        var s=""
      if (list != null) {
        for(ind in list){
            s=s+ind+"\n"
        }
      }
      return s;
    }
}