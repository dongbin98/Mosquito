package com.bonghwan.mosquito.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FcmToken(
    @SerializedName("device_token")
    @Expose
    val deviceToken: String,
)
