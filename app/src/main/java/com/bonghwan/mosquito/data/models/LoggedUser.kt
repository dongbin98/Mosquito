package com.bonghwan.mosquito.data.models

data class LoggedUser(
    var account: Account,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
)
