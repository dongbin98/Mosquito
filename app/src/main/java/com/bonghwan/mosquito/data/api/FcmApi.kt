package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.LoginResponseDto
import com.bonghwan.mosquito.data.api.dto.ReqAccount
import com.bonghwan.mosquito.data.api.dto.ReqFcmToken
import com.bonghwan.mosquito.data.api.dto.ReqLogin
import com.bonghwan.mosquito.data.api.dto.StatusListDto
import com.bonghwan.mosquito.data.models.Account
import com.bonghwan.mosquito.data.models.FcmToken
import com.bonghwan.mosquito.data.models.LoggedUser
import com.bonghwan.mosquito.data.models.Status
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmApi {
    @POST("fcm-token")
    fun createFcmToken(@Body reqFcmToken: ReqFcmToken): Call<FcmToken>

    @DELETE("fcm-token/{device_token}")
    fun deleteFcmToken(@Path("device_token") deviceToken: String): Call<FcmToken>

    @POST("fcm-token/test")
    fun testNotification(): Call<String>
}