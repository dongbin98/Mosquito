package com.bonghwan.mosquito.data.api.dto

import com.bonghwan.mosquito.data.models.Account
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("access_token")
    @Expose
    val accessToken: String,

    @SerializedName("refresh_token")
    @Expose
    val refreshToken: String,

    @SerializedName("token_type")
    @Expose
    val tokenType: String,

    @SerializedName("account")
    @Expose
    val account: Account,
)
