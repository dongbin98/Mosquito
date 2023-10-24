package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.models.Status
import retrofit2.Call
import retrofit2.http.GET

interface MosquitoApi {
    @GET("mosquito/recent")
    fun getMosquitoRecent(): Call<Status>

    @GET("mosquito/week")
    fun getMosquitoWeek(): Call<List<Status>>

    @GET("mosquito/all")
    fun getMosquitoAll(): Call<List<Status>>
}