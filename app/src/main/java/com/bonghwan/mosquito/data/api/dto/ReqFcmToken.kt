package com.bonghwan.mosquito.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReqFcmToken(
    @SerializedName("device_token")
    @Expose
    val deviceToken: String,
)
