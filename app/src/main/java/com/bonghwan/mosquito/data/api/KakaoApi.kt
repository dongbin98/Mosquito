package com.bonghwan.mosquito.data.api

import com.bonghwan.mosquito.data.api.dto.KakaoProfileResponseDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface KakaoApi {
    // 프로필 조회
    @GET("me")
    fun getUserProfile(): Call<KakaoProfileResponseDto>
}