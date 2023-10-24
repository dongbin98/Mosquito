package com.bonghwan.mosquito.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("mosquito_date")
    @Expose
    val date: String?,
    @SerializedName("mosquito_value_water")
    @Expose
    val water: String?,
    @SerializedName("mosquito_value_house")
    @Expose
    val house: String?,
    @SerializedName("mosquito_value_park")
    @Expose
    val park: String?,
)