package com.technzone.miniborsa.data.di

import android.content.Context
import androidx.room.Room
import com.technzone.miniborsa.data.db.ApplicationDB
import com.technzone.miniborsa.data.db.ApplicationDB.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): ApplicationDB {
        return Room.databaseBuilder(
            context,
            ApplicationDB::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}