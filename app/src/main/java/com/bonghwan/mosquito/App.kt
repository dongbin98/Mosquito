package com.bonghwan.mosquito

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class App: Application() {

    init {
        instance = this
    }
    companion object {
        lateinit var instance: App
        fun getInstanceApp(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        /* get metadata for api key */
        val metadata = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            ).metaData
        } else {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        }

        KakaoSdk.init(this, metadata.getString("com.bonghwan.mosquito.kakaoSdkKey").toString())

        Log.d("Kakao Hash", Utility.getKeyHash(this))
    }

    fun makeText(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}