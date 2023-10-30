package com.bonghwan.mosquito.data.api.dto

import com.bonghwan.mosquito.data.models.Status
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusList(
    @SerializedName("COUNT")
    @Expose
    val count: Int,

    @SerializedName("LIST")
    @Expose
    val list: List<Status>
)
