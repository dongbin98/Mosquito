package com.bonghwan.mosquito.data.api

import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


fun provideMosquitoApi(): MosquitoApi = Retrofit.Builder()
    .baseUrl("http://13.125.169.9/mosquito/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(MosquitoApi::class.java)

fun provideAccountApi(): AccountApi = Retrofit.Builder()
    .baseUrl("http://13.125.169.9/account/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(AccountApi::class.java)

fun provideUserDataApi(): UserDataApi = Retrofit.Builder()
    .baseUrl("http://13.125.169.9/userdata/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(UserDataApi::class.java)

fun provideKakaoApiWithToken(accessToken: String): KakaoApi = Retrofit.Builder()
    .baseUrl("https://kapi.kakao.com/v2/user/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(accessTokenClient(accessToken))
    .build()
    .create(KakaoApi::class.java)

fun accessTokenClient(accessToken: String) = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken").build()
        chain.proceed(request)
    }
    .build()