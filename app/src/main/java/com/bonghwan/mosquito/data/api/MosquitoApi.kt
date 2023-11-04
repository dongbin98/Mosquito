package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.StatusListDto
import com.bonghwan.mosquito.data.models.Status
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface MosquitoApi {
    @GET("recent")
    fun getMosquitoRecent(): Call<Status>

    @GET("week")
    fun getMosquitoWeek(): Call<StatusListDto>

    @GET("all")
    fun getMosquitoAll(): Call<StatusListDto>
}