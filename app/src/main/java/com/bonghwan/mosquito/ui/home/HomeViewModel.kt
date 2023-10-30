package com.bonghwan.mosquito.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonghwan.mosquito.data.api.provideMosquitoApi
import com.bonghwan.mosquito.data.models.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeViewModel: ViewModel() {
    private val mosquitoApi = provideMosquitoApi()

    private val _mosquitoLiveData = MutableLiveData<Status>()
    var mosquitoLiveData: LiveData<Status> = _mosquitoLiveData

    private val _mosquitoListLiveData = MutableLiveData<List<Status>>()
    var mosquitoListLiveData: LiveData<List<Status>> = _mosquitoListLiveData

    private val _error = MutableLiveData<String?>()
    var error: MutableLiveData<String?> = _error

    private val apiKey = "7a6143617479656f343074756f4a65"

    private fun handleError(message: String?) {
        _error.postValue(message)
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
}