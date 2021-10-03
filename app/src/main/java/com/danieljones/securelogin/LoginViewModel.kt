package com.danieljones.securelogin

import android.content.Context
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    fun addFailedAttempt(context: Context) {
        // get reference to shared preferences file
        val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_filename), Context.MODE_PRIVATE)
        val currentFailedAttempts = sharedPref.getInt(context.getString(R.string.failed_attempts_key), 0)

        // increment counter
        sharedPref.edit().putInt(context.getString(R.string.failed_attempts_key), currentFailedAttempts + 1).apply()
    }
}