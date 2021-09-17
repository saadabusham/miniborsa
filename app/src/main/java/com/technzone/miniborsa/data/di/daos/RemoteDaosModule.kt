package com.technzone.miniborsa.data.di.daos

import com.technzone.miniborsa.data.daos.remote.business.BusinessRemoteDao
import com.technzone.miniborsa.data.daos.remote.common.CommonRemoteDao
import com.technzone.miniborsa.data.daos.remote.configuration.ConfigurationRemoteDao
import com.technzone.miniborsa.data.daos.remote.investor.InvestorRemoteDao
import com.technzone.miniborsa.data.daos.remote.twilio.TwilioRemoteDao
import com.technzone.miniborsa.data.daos.remote.user.UserRemoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteDaosModule {


    @Singleton
    @Provides
    fun provideUserRemoteDao(
        retrofit: Retrofit
    ): UserRemoteDao {
        return retrofit.create(UserRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideConfigurationRemoteDao(
        retrofit: Retrofit
    ): ConfigurationRemoteDao {
        return retrofit.create(ConfigurationRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideTwillioRemoteDao(
        retrofit: Retrofit
    ): TwilioRemoteDao {
        return retrofit.create(TwilioRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideBusinessRemoteDao(
        retrofit: Retrofit
    ): BusinessRemoteDao {
        return retrofit.create(BusinessRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideInvestorRemoteDao(
        retrofit: Retrofit
    ): InvestorRemoteDao {
        return retrofit.create(InvestorRemoteDao::class.java)
    }

    @Singleton
    @Provides
    fun provideCommonRemoteDao(
        retrofit: Retrofit
    ): CommonRemoteDao {
        return retrofit.create(CommonRemoteDao::class.java)
    }

}