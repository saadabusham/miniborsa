package com.technzone.miniborsa.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.technzone.miniborsa.data.daos.local.searchbusiness.SearchedBusinessLocalDao
import com.technzone.miniborsa.data.db.typeconverter.FieldsListConverter
import com.technzone.miniborsa.data.db.typeconverter.MediaListConverter
import com.technzone.miniborsa.data.db.typeconverter.PropertiesListConverter
import com.technzone.miniborsa.data.models.investor.Business

@Database(
        entities = [
            Business::class
        ], version = 1
)

@TypeConverters(
    MediaListConverter::class,
    PropertiesListConverter::class,
    FieldsListConverter::class
)

abstract class ApplicationDB : RoomDatabase() {

    abstract fun searchedBusinessLocalDao(): SearchedBusinessLocalDao

    companion object {
        const val DATABASE_NAME = "miniborsa.db"
        const val TABLE_SEARCHED_BUSINESS = "miniborsaTable"
    }

}