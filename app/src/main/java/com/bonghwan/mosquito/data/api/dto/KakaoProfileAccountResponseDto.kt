package com.dongbin.popple.data.dto.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KakaoProfileAccountResponseDto(
    @SerializedName("email")
    @Expose
    val email: String?,
)