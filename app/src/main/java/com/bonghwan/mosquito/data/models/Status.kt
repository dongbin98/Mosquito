package com.bonghwan.mosquito.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("MOSQUITO_DATE")
    @Expose
    val date: String?,
    @SerializedName("MOSQUITO_VALUE_WATER")
    @Expose
    val water: String?,
    @SerializedName("MOSQUITO_VALUE_HOUSE")
    @Expose
    val house: String?,
    @SerializedName("MOSQUITO_VALUE_PARK")
    @Expose
    val park: String?,
)