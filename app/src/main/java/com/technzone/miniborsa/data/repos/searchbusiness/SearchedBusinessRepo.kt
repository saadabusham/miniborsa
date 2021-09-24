package com.technzone.miniborsa.data.repos.searchbusiness

import com.technzone.miniborsa.data.models.investor.Business

interface SearchedBusinessRepo {

    suspend fun saveBusiness(business: Business)
    suspend fun saveBusinesses(businesses: List<Business>)
    suspend fun getBusiness(): List<Business>
    suspend fun getBusiness(id: Int): Business
    suspend fun deleteBusiness(id: Int)
    suspend fun clearBusinesses()
}