package com.bonghwan.mosquito

import android.app.Application
import android.widget.Toast

class App: Application() {
    companion object {
        lateinit var instance: App
        fun getInstanceApp(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun makeText(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}