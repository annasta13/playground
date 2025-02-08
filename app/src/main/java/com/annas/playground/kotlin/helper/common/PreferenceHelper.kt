package com.annas.playground.kotlin.helper.common

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.annas.playground.kotlin.utils.Constants

@Suppress("unused")
class PreferenceHelper(context: Context) {
    private val preference =
        context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE)

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedPreference = EncryptedSharedPreferences.create(
        Constants.PREFERENCE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveString(key: String, value: String) {
        preference.edit().putString(key, value).apply()
        encryptedPreference.edit().putString(key, value).apply()
    }
    fun saveEncryptedString(key: String, value: String) {
        encryptedPreference.edit().putString(key, value).apply()
    }

    fun getEncryptedString(key: String): String? {
        return encryptedPreference.getString(key, null)
    }

    fun saveInt(key: String, value: Int) {
        preference.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return preference.getInt(key, 0)
    }

    fun getString(key: String): String? {
        return preference.getString(key, null)
    }

    fun clear() {
        preference.edit().clear().apply()
        encryptedPreference.edit().clear().apply()
    }
}
