package com.annas.playground.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.annas.playground.utils.Constants
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import androidx.core.content.edit

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
        preference.edit { putString(key, value) }
        encryptedPreference.edit { putString(key, value) }
    }
    fun saveEncryptedString(key: String, value: String) {
        encryptedPreference.edit { putString(key, value) }
    }

    fun getEncryptedString(key: String): String? {
        return encryptedPreference.getString(key, null)
    }

    fun saveInt(key: String, value: Int) {
        preference.edit { putInt(key, value) }
    }

    fun getInt(key: String): Int {
        return preference.getInt(key, 0)
    }

    fun getString(key: String): String? {
        return preference.getString(key, null)
    }

    fun clear() {
        preference.edit { clear() }
        encryptedPreference.edit { clear() }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PreferenceHelperInitializer {
    val preference: PreferenceHelper
}

@Composable
fun rememberPreferenceHelper(): PreferenceHelper {
    // Get the current Android Context from the composition.
    val context = LocalContext.current
    // Remember the PreferenceHelper instance.
    // It will be created only once for the given context and reused across recompositions.
    val state by remember {
        lazy {
            EntryPointAccessors.fromApplication(
                context,
                PreferenceHelperInitializer::class.java
            ).preference
        }
    }
    return state
}
