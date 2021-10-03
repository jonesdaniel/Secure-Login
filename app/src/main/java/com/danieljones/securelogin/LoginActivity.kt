package com.danieljones.securelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.danieljones.securelogin.databinding.ActivityLoginBinding
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var layout: ActivityLoginBinding
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(layout.root)

        layout.loginButton.setOnClickListener {
            showBiometricPrompt()
        }

        initializeViewModel()
    }

    private fun initializeViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) { // user canceled dialog
                    showAlert(errString.toString())
                }

                if (errorCode == BiometricPrompt.ERROR_LOCKOUT) {
                    loginViewModel.addFailedAttempt(this@LoginActivity)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                loginViewModel.addFailedAttempt(this@LoginActivity)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showWelcomeScreen()
            }
        }

        biometricPrompt = BiometricPrompt(this, executor, callback)
        biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        biometricPrompt.authenticate(biometricPromptInfo)
    }

    private fun showAlert(errorString: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(errorString)
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setIcon(R.drawable.fingerprint_dialog_error)

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.error))
        alert.show()
    }

    private fun showWelcomeScreen() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}