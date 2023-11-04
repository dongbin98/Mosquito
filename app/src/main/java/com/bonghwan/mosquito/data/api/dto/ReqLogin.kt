package com.bonghwan.mosquito.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReqLogin(
    val username: String? = null,
    val password: String? = null,
    @SerializedName("refresh_token")
    @Expose
    val refreshToken: String? = null,
)
