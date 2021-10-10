package com.technzone.minibursa.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.technzone.minibursa.data.daos.local.searchbusiness.SearchedBusinessLocalDao
import com.technzone.minibursa.data.db.typeconverter.*
import com.technzone.minibursa.data.models.investor.Business

@Database(
        entities = [
            Business::class
        ], version = 1
)

@TypeConverters(
    MediaListConverter::class,
    PropertiesListConverter::class,
    FieldsListConverter::class,
    CategoriesListConverter::class,
    LookupListConverter::class,
    CountriesListConverter::class
)

abstract class ApplicationDB : RoomDatabase() {

    abstract fun searchedBusinessLocalDao(): SearchedBusinessLocalDao

    companion object {
        const val DATABASE_NAME = "minibursa.db"
        const val TABLE_SEARCHED_BUSINESS = "miniborsaTable"
    }

}