package com.bookstore.admin.utils

import android.util.Log
import com.bookstore.admin.config.AppConfig.API_BASE_URL
import com.bookstore.admin.config.AppConfig.OAUTH_DEFAULT_CLIENT_ID
import com.bookstore.admin.config.AppConfig.OAUTH_DEFAULT_CLIENT_SECRET
import com.bookstore.admin.config.AppConfig.OAUTH_DEFAULT_USER_AGENT
import com.bookstore.admin.config.AppConfig.RETROFIT_TIMEOUT
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object Retrofit {

    fun getClient(baseUrl: String = API_BASE_URL): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor {
                        val original = it.request()
                        val request = original.newBuilder().apply {
                            if (original.header("Authorization") == null) header(
                                "Authorization",
                                Credentials.basic(
                                    OAUTH_DEFAULT_CLIENT_ID,
                                    OAUTH_DEFAULT_CLIENT_SECRET
                                )
                            )
                            header("User-Agent", OAUTH_DEFAULT_USER_AGENT)
                            method(original.method(), original.body())
                        }.build()
                        it.proceed(request)
                    }
                    .connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun Throwable.printRetrofitError() {
        this.printStackTrace()
        when (this) {
            is IOException -> Log.e(
                this::class.java.simpleName,
                "Network Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            is HttpException -> Log.e(
                this::class.java.simpleName,
                "HTTP Exception happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            else -> Log.e(
                this::class.java.simpleName,
                "Unknown Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
        }
    }
}