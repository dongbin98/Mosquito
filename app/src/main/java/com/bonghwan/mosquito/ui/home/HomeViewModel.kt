package com.bonghwan.mosquito.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonghwan.mosquito.data.api.dto.PartUserData
import com.bonghwan.mosquito.data.api.dto.ReqFcmToken
import com.bonghwan.mosquito.data.api.dto.ReqUserData
import com.bonghwan.mosquito.data.api.provideFcmApi
import com.bonghwan.mosquito.data.api.provideMosquitoApi
import com.bonghwan.mosquito.data.api.provideUserDataApi
import com.bonghwan.mosquito.data.models.FcmToken
import com.bonghwan.mosquito.data.models.Status
import com.bonghwan.mosquito.data.models.UserData
import com.kakao.sdk.user.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeViewModel: ViewModel() {
    private val mosquitoApi = provideMosquitoApi()
    private val userDataApi = provideUserDataApi()
    private val fcmApi = provideFcmApi()

    private val _mosquitoLiveData = MutableLiveData<Status>()
    var mosquitoLiveData: LiveData<Status> = _mosquitoLiveData

    private val _mosquitoListLiveData = MutableLiveData<List<Status>>()
    var mosquitoListLiveData: LiveData<List<Status>> = _mosquitoListLiveData

    private val _error = MutableLiveData<String?>()
    var error: LiveData<String?> = _error

    private val _userDataCreateLiveData = MutableLiveData<UserData>()
    var userDataCreateLiveData: LiveData<UserData> = _userDataCreateLiveData

    private val _userDataGetLiveData = MutableLiveData<UserData>()
    var userDataGetLiveData: LiveData<UserData> = _userDataGetLiveData

    private val _userDataUpdateLiveData = MutableLiveData<UserData>()
    var userDataUpdateLiveData: LiveData<UserData> = _userDataUpdateLiveData

    private val _userDataDeleteLiveData = MutableLiveData<UserData>()
    var userDataDeleteLiveData: LiveData<UserData> = _userDataDeleteLiveData

    private val _userDataError = MutableLiveData<String?>()
    var userDataError: LiveData<String?> = _userDataError

    private val apiKey = "7a6143617479656f343074756f4a65"

    private fun handleError(message: String?) {
        _error.postValue(message)
    }

    private fun handleUserDataError(message: String?) {
        _userDataError.postValue(message)
    }

    suspend fun getMosquitoRecent() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    mosquitoApi.getMosquitoRecent().execute()
                }
                if (response.isSuccessful) {
                    _mosquitoLiveData.postValue(response.body())
                    Log.d("data", response.body().toString())
                } else {
                    handleError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleError(e.message.toString())
            }
        }
    }

    suspend fun getMosquitoWeek() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    mosquitoApi.getMosquitoWeek().execute()
                }
                if (response.isSuccessful) {
                    _mosquitoListLiveData.postValue(response.body()?.list)
                    Log.d("data", response.body().toString())
                } else {
                    handleError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleError(e.message.toString())
            }
        }
    }

    suspend fun getMosquitoAll() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    mosquitoApi.getMosquitoAll().execute()
                }
                if (response.isSuccessful) {
                    _mosquitoListLiveData.postValue(response.body()?.list)
                    Log.d("data", response.body().toString())
                } else {
                    handleError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleError(e.message.toString())
            }
        }
    }

    suspend fun createUserData(reqUserData: ReqUserData) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userDataApi.createUserData(reqUserData).execute()
                }
                if (response.isSuccessful) {
                    _userDataCreateLiveData.postValue(response.body())
                } else {
                    handleUserDataError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleUserDataError(e.message.toString())
            }
        }
    }

    suspend fun getUserData(id: String, date: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userDataApi.getUserData(id, date).execute()
                }
                if (response.isSuccessful) {
                    _userDataGetLiveData.postValue(response.body())
                } else {
//                    handleUserDataError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleUserDataError(e.message.toString())
            }
        }
    }

    suspend fun updateUserData(id: String, date: String, count: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userDataApi.updateUserData(id, date, count).execute()
                }
                if (response.isSuccessful) {
                    _userDataUpdateLiveData.postValue(response.body())
                } else {
                    handleUserDataError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleUserDataError(e.message.toString())
            }
        }
    }

    suspend fun deleteUserData(id: String, date: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userDataApi.deleteUserData(id, date).execute()
                }
                if (response.isSuccessful) {
                    _userDataDeleteLiveData.postValue(response.body())
                } else {
                    handleUserDataError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleUserDataError(e.message.toString())
            }
        }
    }
}