package com.bonghwan.mosquito.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideMosquitoApi(): MosquitoApi = Retrofit.Builder()
    .baseUrl("http://openapi.seoul.go.kr:8088/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient())
    .build()
    .create(MosquitoApi::class.java)
