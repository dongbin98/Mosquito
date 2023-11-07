package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.LoginResponseDto
import com.bonghwan.mosquito.data.api.dto.PartUserData
import com.bonghwan.mosquito.data.api.dto.ReqAccount
import com.bonghwan.mosquito.data.api.dto.ReqLogin
import com.bonghwan.mosquito.data.api.dto.ReqUserData
import com.bonghwan.mosquito.data.api.dto.StatusListDto
import com.bonghwan.mosquito.data.models.Account
import com.bonghwan.mosquito.data.models.LoggedUser
import com.bonghwan.mosquito.data.models.Status
import com.bonghwan.mosquito.data.models.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserDataApi {
    @POST(" ")
    fun createUserData(@Body reqUserData: ReqUserData): Call<UserData>

    @GET("{id}/{date}")
    fun getUserData(@Path("id") id: String, @Path("date") date: String): Call<UserData>

    @PUT("{id}/{date}")
    fun updateUserData(@Path("id") id: String, @Path("date") date: String, @Query("count") count: String): Call<UserData>

    @DELETE("{id}/{date}")
    fun deleteUserData(@Path("id") id: String, @Path("date") date: String): Call<UserData>
}