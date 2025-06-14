package com.example.fluent.data.remote

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.fluent.data.dto.TokenResponseDto
import com.example.fluent.data.remote.Constants.KEY_ACCESS_TOKEN
import com.example.fluent.data.remote.Constants.KEY_REFRESH_TOKEN

class TokenManager(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_token_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(tokens: TokenResponseDto) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, tokens.accessToken)
            putString(KEY_REFRESH_TOKEN, tokens.refreshToken)
        }
    }

    fun hasAccessToken(): Boolean =
        getAccessToken() != null && getAccessToken()?.isNotBlank() == true

    fun hasRefreshToken(): Boolean =
        getRefreshToken() != null && getRefreshToken()?.isNotBlank() == true

    fun isLoggedIn(): Boolean = hasAccessToken() == true && hasRefreshToken() == true

    fun getAccessToken(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)

    fun clearTokens() {
        sharedPreferences.edit { clear() }
    }
}