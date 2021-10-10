package com.technzone.minibursa.data.daos.local.searchbusiness

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.technzone.minibursa.data.db.ApplicationDB.Companion.TABLE_SEARCHED_BUSINESS
import com.technzone.minibursa.data.models.investor.Business

@Dao
interface SearchedBusinessLocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBusiness(business: Business)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBusinesses(businesses: List<Business>)

    @Query("SELECT * FROM $TABLE_SEARCHED_BUSINESS ORDER BY id ASC")
    suspend fun getBusiness(): List<Business>

    @Query("SELECT * FROM $TABLE_SEARCHED_BUSINESS WHERE id = :id")
    suspend fun getBusiness(id: Int): Business

    @Query("DELETE FROM $TABLE_SEARCHED_BUSINESS WHERE id = :id")
    suspend fun deleteBusiness(id: Int)

    @Query("DELETE FROM $TABLE_SEARCHED_BUSINESS")
    suspend fun clearBusinesses()
}