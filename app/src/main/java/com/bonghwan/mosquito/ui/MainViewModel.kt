package com.bonghwan.mosquito.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonghwan.mosquito.data.api.dto.MosquitoStatusDto
import com.bonghwan.mosquito.data.api.provideMosquitoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainViewModel: ViewModel() {
    var isReady = false
    private val mosquitoApi = provideMosquitoApi()

    private val _mosquitoLiveData = MutableLiveData<MosquitoStatusDto>()
    var mosquitoLiveData: LiveData<MosquitoStatusDto> = _mosquitoLiveData

    private val _error = MutableLiveData<String?>()
    var error: MutableLiveData<String?> = _error

    private val apiKey = "7a6143617479656f343074756f4a65"

    private fun handleError(message: String?) {
        _error.postValue(message)
    }

    suspend fun getMosquitoStatus(date: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    mosquitoApi.getMosquitoStatus(apiKey, date).execute()
                }
                if (response.isSuccessful) {
                    if (response.body()?.mosquitoStatus == null) {
                        handleError(response.body()?.result?.message)
                    } else {
                        handleError("해당 데이터를 가져옵니다.")
                        _mosquitoLiveData.postValue(response.body())
                    }
                    Log.d("data", response.body().toString())
                } else {
                    handleError(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleError(e.message.toString())
            }
            isReady = true
        }
    }
}