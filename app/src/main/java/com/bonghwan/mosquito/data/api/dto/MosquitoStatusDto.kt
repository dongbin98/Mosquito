package com.bonghwan.mosquito.data.api.dto

import com.bonghwan.mosquito.data.models.Status
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MosquitoStatusDto(
    @SerializedName("MosquitoStatus")
    @Expose
    val mosquitoStatus: MosquitoStatus?,
    @SerializedName("RESULT")
    @Expose
    val result: ApiResult?
)

data class ApiResult(
    @SerializedName("CODE")
    @Expose
    val code: String,
    @SerializedName("MESSAGE")
    @Expose
    val message: String,
)

data class MosquitoStatus(
    @SerializedName("RESULT")
    @Expose
    val result: ApiResult,
    @SerializedName("row")
    @Expose
    val row: List<Status>?,
)