package com.vaskevicius.android.joky.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesUtil constructor(context: Context) {
    companion object {
        const val PREF_NAME = "joky.shared.preferences"
        private const val IS_SAFE_MODE_ENABLED = "pref.safemode.bool"
        private const val IS_FIRST_TIME_LAUNCH = "pref.is.first.time"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun enableSafeMode(enable: Boolean) = prefs.edit { putBoolean(IS_SAFE_MODE_ENABLED, enable).commit() }

    fun isSafeModeEnabled():Boolean = prefs.getBoolean(IS_SAFE_MODE_ENABLED, true)

    var isFirstTimeLaunch: Boolean
        get() {
            return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        }
        set(isFirstTime) {
            prefs.edit { putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime).commit()}
        }
}