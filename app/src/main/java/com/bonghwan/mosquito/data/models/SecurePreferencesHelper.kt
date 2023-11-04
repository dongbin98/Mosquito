package com.bonghwan.mosquito.data.models

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePreferencesHelper {
    private const val PREF_NAME = "my_secure_prefs"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    fun saveRefreshToken(context: Context, refreshToken: String) {
        try {
            // Create a MasterKey for encryption
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Initialize EncryptedSharedPreferences
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Save the refresh token
            val editor = sharedPreferences.edit()
            editor.putString(KEY_REFRESH_TOKEN, refreshToken)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRefreshToken(context: Context): String? {
        try {
            // Create a MasterKey for decryption
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Initialize EncryptedSharedPreferences
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Retrieve the refresh token
            return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
