package com.bonghwan.mosquito.data.api.dto

data class PartUserData(
    val date: String? = null,
    val count: String? = null,
) {
    fun toMap() = mapOf(
        "date" to date,
        "count" to count
    )
}
