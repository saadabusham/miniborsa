package com.technzone.miniborsa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.miniborsa.data.models.Media
import java.lang.reflect.Type

class MediaListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<Media>? {
        val listType: Type = object : TypeToken<List<Media>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<Media>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}