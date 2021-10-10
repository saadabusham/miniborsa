package com.technzone.minibursa.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.pref.configuration.ConfigurationPrefImp
import com.technzone.minibursa.data.pref.favorite.FavoritePref
import com.technzone.minibursa.data.pref.favorite.FavoritePrefImp
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.data.pref.user.UserPrefImp
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PrefModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesUtil(@ApplicationContext context: Context): SharedPreferencesUtil {
        return SharedPreferencesUtil.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideConfigurationPref(preferencesUtil: SharedPreferencesUtil): ConfigurationPref {
        return ConfigurationPrefImp(preferencesUtil)
    }

    @Provides
    @Singleton
    fun provideUserPref(preferencesUtil: SharedPreferencesUtil): UserPref {
        return UserPrefImp(preferencesUtil)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    fun provideFavoritePreferences(preferencesUtil: SharedPreferencesUtil): FavoritePref {
        return FavoritePrefImp(preferencesUtil)
    }
}