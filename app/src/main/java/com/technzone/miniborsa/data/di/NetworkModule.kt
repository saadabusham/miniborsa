package com.technzone.miniborsa.data.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.technzone.miniborsa.BuildConfig
import com.technzone.miniborsa.data.common.NetworkConstants.APP_BASE_URL
import com.technzone.miniborsa.data.common.NetworkConstants.APP_TIMEOUT_MINUTES
import com.technzone.miniborsa.data.interceptors.AppBaseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideInterceptor(
//        configurationPref: ConfigurationPref,
//        userPref: UserPref
//    ): AppBaseInterceptor = AppBaseInterceptor(
//        configurationPref,
//        userPref
//    )

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        interceptor: AppBaseInterceptor,
        loggerInterceptor: HttpLoggingInterceptor,
        application: Application
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .readTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .writeTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)
            .connectTimeout(APP_TIMEOUT_MINUTES, TimeUnit.MINUTES)
        if (BuildConfig.DEBUG)
            okHttpClient
                .addInterceptor(ChuckInterceptor(application))
                .addInterceptor(loggerInterceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    request.addHeader("Accept", "application/json")

                    val response = chain.proceed(request.build())
                    if (response.code == 401) {
                        EventBus.getDefault().post(response.code.toString())
                    }

                    response

                }

        return okHttpClient.build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }
}