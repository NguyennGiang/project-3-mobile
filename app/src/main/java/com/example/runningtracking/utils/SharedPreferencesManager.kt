package com.example.runningtracking.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "RunSharedPreference", Activity.MODE_PRIVATE
    )

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    private val gson = Gson()

    private companion object {
        const val KEY_TOKEN = "key_token"
        const val KEY_REFRESH_TOKEN = "key_refresh_token"
        const val KEY_IS_SET_UP_PERSONAL_DATA = "key_is_set_up_personal_data"
        const val KEY_USER_NAME = "key_user_name"
        const val KEY_USER_WEIGHT = "key_user_weight"
    }

    var token: String
        get() = getString(KEY_TOKEN, "") ?: ""
        set(value) = setString(KEY_TOKEN, value)

    var refreshToken: String
        get() = getString(KEY_REFRESH_TOKEN, "") ?: ""
        set(value) = setString(KEY_REFRESH_TOKEN, value)

    var isSetupPersonalData: Boolean
        get() = getBoolean(KEY_IS_SET_UP_PERSONAL_DATA, false)
        set(value) = setBoolean(KEY_IS_SET_UP_PERSONAL_DATA, value)

    var userName: String
        get() = getString(KEY_USER_NAME, "") ?: ""
        set(value) = setString(KEY_USER_NAME, value)
    var userWeight: Float
        get() = getFloat(KEY_USER_WEIGHT, 0.0f)
        set(value) = setFloat(KEY_USER_WEIGHT, value)

    private fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    private fun getLong(key: String, defaultValue: Long = 0): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    private fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    private fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    private fun setInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    private fun setLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }

    private fun getFloat(key: String, defaultValue: Float = 0.0F): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    private fun setFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }
}
