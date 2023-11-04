package com.bonghwan.mosquito.ui.intro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonghwan.mosquito.data.api.AccountApi
import com.bonghwan.mosquito.data.api.KakaoApi
import com.bonghwan.mosquito.data.api.dto.KakaoProfileResponseDto
import com.bonghwan.mosquito.data.api.dto.LoginResponseDto
import com.bonghwan.mosquito.data.api.dto.ReqAccount
import com.bonghwan.mosquito.data.api.dto.ReqLogin
import com.bonghwan.mosquito.data.api.provideAccountApi
import com.bonghwan.mosquito.data.api.provideKakaoApiWithToken
import com.bonghwan.mosquito.data.models.Account
import com.bonghwan.mosquito.data.models.ErrorResponse
import com.bonghwan.mosquito.util.EventWrapper
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception

class IntroViewModel : ViewModel() {
    var isReady = false

    private val accountApi: AccountApi = provideAccountApi()
    private lateinit var kakaoApi: KakaoApi

    private val _accountResponse = MutableLiveData<Boolean>()
    var accountResponse: LiveData<Boolean> = _accountResponse

    private val _createResponse = MutableLiveData<Account>()
    val createResponse: LiveData<Account> = _createResponse

    private val _loginResponse = MutableLiveData<LoginResponseDto?>()
    var loginResponse: LiveData<LoginResponseDto?> = _loginResponse

    private val _autoLoginResponse = MutableLiveData<LoginResponseDto?>()
    var autoLoginResponse: LiveData<LoginResponseDto?> = _autoLoginResponse

    private val _kakaoProfile = MutableLiveData<KakaoProfileResponseDto>()
    var kakaoProfile: LiveData<KakaoProfileResponseDto> = _kakaoProfile

    private val _error = MutableLiveData<String?>()
    var error: LiveData<String?> = _error

    private val gson = Gson()

    private fun handleError(message: String?) {
        _error.postValue(message)
    }

    suspend fun createAccount(username: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val reqAccount: ReqAccount = ReqAccount(username, password, name)
                    Log.d("reqAccount", reqAccount.toString())
                    accountApi.createAccount(reqAccount).execute()
                }
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    _createResponse.postValue(response.body())
                } else {
                    try {
                        val errorResponse = gson.fromJson(response.errorBody()?.string().toString(), ErrorResponse::class.java)
                        if (errorResponse.detail.user == "데이터를 찾을 수 없습니다.") {
                            handleError("회원가입 실패")
                        }
                        else
                            handleError(errorResponse.detail.user)
                    } catch (e: Exception) {
                        handleError("서버 통신 오류")
                    }
                }
            } catch (e: IOException) {
                handleError("인터넷 연결을 확인해주세요.")
                Log.e("error", e.message.toString())
            }
        }
    }

    suspend fun getAccount(username: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    accountApi.getAccount(username).execute()
                }
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    _accountResponse.postValue(true)
                } else {
                    _accountResponse.postValue(false)
                    try {
                        val errorResponse = gson.fromJson(response.errorBody()?.string().toString(), ErrorResponse::class.java)
                        if (errorResponse.detail.user == "데이터를 찾을 수 없습니다.") {
                            handleError("로그인에 실패하였습니다.")
                        }
                        else
                            handleError(errorResponse.detail.user)
                    } catch (e: Exception) {
                        handleError("서버 통신 오류")
                    }
                }
            } catch (e: IOException) {
                handleError("인터넷 연결을 확인해주세요.")
                Log.e("error", e.message.toString())
            }
        }
    }

    suspend fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    accountApi.login(ReqLogin(username=username, password=password)).execute()
                }
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    _loginResponse.postValue(response.body())
                } else {
                    try {
                        val errorResponse = gson.fromJson(response.errorBody()?.string().toString(), ErrorResponse::class.java)
                        if (errorResponse.detail.user == "데이터를 찾을 수 없습니다.")
                            handleError("로그인에 실패하였습니다.")
                        else
                            handleError(errorResponse.detail.user)
                    } catch (e: Exception) {
                        handleError("서버 통신 오류")
                    }
                }
            } catch (e: IOException) {
                handleError("인터넷 연결을 확인해주세요.")
                Log.e("error", e.message.toString())
            }
        }
    }

    suspend fun autoLogin(refreshToken: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    accountApi.login(ReqLogin(refreshToken=refreshToken)).execute()
                }
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    _autoLoginResponse.postValue(response.body())
                } else {
                    try {
                        _autoLoginResponse.postValue(null)
                        val errorResponse = gson.fromJson(response.errorBody()?.string().toString(), ErrorResponse::class.java)
                        if (errorResponse.detail.user == "데이터를 찾을 수 없습니다.")
                            handleError("로그인에 실패하였습니다.")
                        else
                            handleError(errorResponse.detail.user)
                    } catch (e: Exception) {
                        handleError("서버 통신 오류")
                    }
                }
            } catch (e: IOException) {
                handleError("인터넷 연결을 확인해주세요.")
                Log.e("error", e.message.toString())
            }
        }
    }

    suspend fun getKakaoProfile(accessToken: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    kakaoApi = provideKakaoApiWithToken(accessToken)
                    kakaoApi.getUserProfile().execute()
                }
                if (response.isSuccessful) {
                    _kakaoProfile.postValue(response.body())
                } else {
                    handleError("카카오 계정 인증 실패")
                }
            } catch (e: IOException) {
                handleError("인터넷 연결을 확인해주세요.")
                Log.e("error", e.message.toString())
            }
        }
    }
}