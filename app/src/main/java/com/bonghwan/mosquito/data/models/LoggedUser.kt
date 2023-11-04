package com.bonghwan.mosquito.data.models

import com.bonghwan.mosquito.data.api.dto.TokenResponseDto

data class LoggedUser(
    var account: Account,
    val token: TokenResponseDto
)
