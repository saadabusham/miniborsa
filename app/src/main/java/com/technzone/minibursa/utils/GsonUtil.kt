package com.technzone.minibursa.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {

    private var gson: Gson = GsonBuilder().create()

    fun <T> fromJsonToObject(json: String?, clazz: Class<T>?): T {
        return gson.fromJson(json, clazz)
    }

    fun toJson(src: Any?): String {
        if (src == null) {
            return ""
        }
        return gson.toJson(src)
    }

    fun <T> fromJsonToList(json: String?): List<T>? {
        val listType: Type = object : TypeToken<List<T>?>() {}.type
        return gson.fromJson(json, listType)
    }
}