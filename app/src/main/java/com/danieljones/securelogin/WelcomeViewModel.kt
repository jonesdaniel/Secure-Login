package com.danieljones.securelogin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    private val displayNumberOfFailedAttempts = MutableLiveData<Int>()

    // Returns a read-only LiveData
    fun getFailedAttempts(context: Context) : LiveData<Int> {

        if (displayNumberOfFailedAttempts.value == null) {
            // get reference to shared preferences
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_pref_filename),
                Context.MODE_PRIVATE
            )

            displayNumberOfFailedAttempts.value =
                sharedPref.getInt(context.getString(R.string.failed_attempts_key), 0)

            // set counter back to 0 since we've logged in successfully
            sharedPref.edit().putInt(context.getString(R.string.failed_attempts_key), 0).apply()
        }

        return displayNumberOfFailedAttempts
    }
}