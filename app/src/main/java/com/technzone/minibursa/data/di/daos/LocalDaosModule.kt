package com.technzone.minibursa.data.di.daos

import com.technzone.minibursa.data.daos.local.searchbusiness.SearchedBusinessLocalDao
import com.technzone.minibursa.data.db.ApplicationDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDaosModule {

    @Provides
    @Singleton
    fun provideCartLocalDao(
        applicationDB: ApplicationDB
    ): SearchedBusinessLocalDao {
        return applicationDB.searchedBusinessLocalDao()
    }

}