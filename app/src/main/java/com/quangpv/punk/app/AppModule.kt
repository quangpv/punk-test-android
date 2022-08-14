package com.quangpv.punk.app

import android.app.Application
import android.support.di.module
import com.google.gson.GsonBuilder
import com.quangpv.punk.helper.network.ApiErrorHandlerImpl
import com.quangpv.punk.helper.network.AsyncAdapterFactory
import com.quangpv.punk.helper.network.TLSSocketFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


val appModule = module {
    single {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    single {
        val application: Application = get()
        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        val cache = Cache(cacheDir, 10485760L) // 10mb
        val tlsSocketFactory = TLSSocketFactory()
        OkHttpClient.Builder()
            .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.systemDefaultTrustManager())
            .cache(cache)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create(
            GsonBuilder().create()
        )
    }

    factory<Retrofit.Builder> {
        Retrofit.Builder()
            .addConverterFactory(get())
            .addCallAdapterFactory(AsyncAdapterFactory(ApiErrorHandlerImpl()))
            .client(get())
    }

    single<Retrofit> {
        get<Retrofit.Builder>()
            .baseUrl("https://api.punkapi.com/v2/")
            .build()
    }

}