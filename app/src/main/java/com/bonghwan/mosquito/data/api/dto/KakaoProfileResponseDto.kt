package com.bonghwan.mosquito.data.api.dto

import com.dongbin.popple.data.dto.login.KakaoProfileAccountResponseDto
import com.dongbin.popple.data.dto.login.KakaoProfilePropertiesResponseDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KakaoProfileResponseDto(
    @SerializedName("id")   // 동일인 식별 정보
    @Expose
    val id: String?,

    @SerializedName("properties")
    @Expose
    val properties: KakaoProfilePropertiesResponseDto?,

    @SerializedName("kakao_account")
    @Expose
    val kakaoAccount: KakaoProfileAccountResponseDto?,
)