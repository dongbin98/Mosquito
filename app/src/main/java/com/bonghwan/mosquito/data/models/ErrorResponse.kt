package com.bonghwan.mosquito.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail")
    @Expose
    val detail: Detail
)

data class Detail(
    @SerializedName("dev")
    @Expose
    val dev: String,
    @SerializedName("user")
    @Expose
    val user: String
)