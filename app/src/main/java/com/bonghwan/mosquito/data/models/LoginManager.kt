package com.bonghwan.mosquito.data.models

import com.bonghwan.mosquito.data.api.dto.LoginResponseDto

object LoginManager {
    private var currentUser: LoggedUser? = null

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    fun login(loginResponseDto: LoginResponseDto) {
        currentUser = LoggedUser(account = loginResponseDto.account, accessToken = loginResponseDto.accessToken, refreshToken = loginResponseDto.refreshToken, tokenType = loginResponseDto.tokenType)
    }

    fun syncInfo(account: Account) {
        currentUser?.account = account
    }

    fun logout() {
        currentUser = null
    }

    fun getCurrentUser(): LoggedUser? {
        return if (currentUser != null)
            currentUser
        else
            null
    }
}
