package com.bonghwan.mosquito.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var isReady = false

    private val _toast = MutableLiveData<String>()
    var toast: LiveData<String> = _toast



    suspend fun setReady() {
        viewModelScope.launch {
            _toast.postValue("데이터 로딩중입니다")
            delay(2000)
            isReady = true
        }
    }
}