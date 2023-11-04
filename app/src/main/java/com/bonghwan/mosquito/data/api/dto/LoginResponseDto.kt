package com.bonghwan.mosquito.data.api.dto

import com.bonghwan.mosquito.data.models.Account
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("token")
    @Expose
    val token: TokenResponseDto,

    @SerializedName("account")
    @Expose
    val account: Account,
)
