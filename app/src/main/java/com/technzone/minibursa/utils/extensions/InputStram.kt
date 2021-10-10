package com.technzone.minibursa.utils.extensions

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

fun readRawJsonAsString(context: Context, @RawRes rawResId: Int): String {
    context.resources.openRawResource(rawResId).bufferedReader().use {
        BufferedReader::readText
        it.readLines().joinToString("")
    }.also {
        return it
    }
}

inline fun <reified T> readRawJson(context: Context, @RawRes rawResId: Int): T {
    context.resources.openRawResource(rawResId).bufferedReader().use {
        return Gson().fromJson<T>(it, object : TypeToken<T>() {}.type)
    }
}