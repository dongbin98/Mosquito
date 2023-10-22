package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.MosquitoStatusDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MosquitoApi {
    @GET("{apiKey}/json/MosquitoStatus/1/5/{date}")
    // date type is yyyy-MM-dd
    fun getMosquitoStatus(
        @Path("apiKey") apiKey: String,
        @Path("date") date: String): Call<MosquitoStatusDto>
}