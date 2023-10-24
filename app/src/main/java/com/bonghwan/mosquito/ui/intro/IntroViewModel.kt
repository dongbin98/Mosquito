package com.bonghwan.mosquito.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {
    var isReady = false

    suspend fun isUser() {
        viewModelScope.launch {
            delay(2000)
            isReady = true
        }
    }
}