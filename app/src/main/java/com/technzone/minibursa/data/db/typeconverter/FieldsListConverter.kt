package com.technzone.minibursa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.minibursa.data.models.investor.FieldsItem
import java.lang.reflect.Type

class FieldsListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<FieldsItem>? {
        val listType: Type = object : TypeToken<List<FieldsItem>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<FieldsItem>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}