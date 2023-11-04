package com.bonghwan.mosquito.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideMosquitoApi(): MosquitoApi = Retrofit.Builder()
    .baseUrl("http://13.125.169.9/mosquito/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(baseClient())
    .build()
    .create(MosquitoApi::class.java)

fun provideAccountApi(): AccountApi = Retrofit.Builder()
    .baseUrl("http://13.125.169.9/account/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(baseClient())
    .build()
    .create(AccountApi::class.java)

fun provideKakaoApiWithToken(accessToken: String): KakaoApi = Retrofit.Builder()
    .baseUrl("https://kapi.kakao.com/v2/user/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(accessTokenClient(accessToken))
    .build()
    .create(KakaoApi::class.java)


fun accessTokenClient(accessToken: String) = OkHttpClient.Builder()
    .connectTimeout(2, TimeUnit.MINUTES)
    .readTimeout(2, TimeUnit.MINUTES)
    .writeTimeout(2, TimeUnit.MINUTES)
    .followRedirects(false)
    .followSslRedirects(false)
    .retryOnConnectionFailure(false)
    .cache(null)
    .addInterceptor(
    Interceptor { chain ->
        val request = chain.request()
        if (request.header("Authorization") == null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            return@Interceptor chain.proceed(newRequest)
        } else {
            // "Authorization" 헤더가 이미 설정된 경우, 추가 작업 없이 요청 진행
            return@Interceptor chain.proceed(request)
        }
    }).build()


fun baseClient() = OkHttpClient.Builder()
    .connectTimeout(2, TimeUnit.MINUTES)
    .readTimeout(2, TimeUnit.MINUTES)
    .writeTimeout(2, TimeUnit.MINUTES)
    .followRedirects(false)
    .followSslRedirects(false)
    .retryOnConnectionFailure(false)
    .cache(null).build()