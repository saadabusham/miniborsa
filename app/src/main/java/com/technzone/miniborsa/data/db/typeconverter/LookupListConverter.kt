package com.technzone.miniborsa.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technzone.miniborsa.data.models.business.business.PropertiesItem
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import java.lang.reflect.Type

class LookupListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<GeneralLookup>? {
        val listType: Type = object : TypeToken<List<PropertiesItem>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<GeneralLookup>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}