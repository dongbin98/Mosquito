package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.LoginResponseDto
import com.bonghwan.mosquito.data.api.dto.ReqAccount
import com.bonghwan.mosquito.data.api.dto.ReqLogin
import com.bonghwan.mosquito.data.api.dto.StatusListDto
import com.bonghwan.mosquito.data.models.Account
import com.bonghwan.mosquito.data.models.LoggedUser
import com.bonghwan.mosquito.data.models.Status
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApi {
    @POST(" ")
    fun createAccount(@Body reqAccount: ReqAccount): Call<Account>

    @GET("{username}")
    fun getAccount(@Path("username") username: String): Call<Account>

    @DELETE("{username}")
    fun deleteAccount(@Path("username") username: String): Call<Account>

    @POST("login")
    fun login(@Body reqLogin: ReqLogin): Call<LoginResponseDto>
}