package com.danieljones.securelogin

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.danieljones.securelogin.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var layout: ActivityWelcomeBinding
    private lateinit var welcomeViewModel: WelcomeViewModel
    private lateinit var animatedVectorDrawable: AnimatedVectorDrawable
    private lateinit var animatedVectorDrawableCompat: AnimatedVectorDrawableCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityWelcomeBinding.inflate(layoutInflater)
        // hiding preview in recent apps
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(layout.root)

        initializeViewModel()

        setupCheckmark()
    }

    private fun initializeViewModel() {
        welcomeViewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        welcomeViewModel.getFailedAttempts(this).observe(this, { failedAttempts ->
            layout.numberTextView.text = failedAttempts.toString()
        })
    }

    // animate checkmark
    private fun setupCheckmark() {
        val checkmark = layout.checkImageView.drawable

        if (checkmark is AnimatedVectorDrawable) {
            animatedVectorDrawable = checkmark
            animatedVectorDrawable.start()
        } else if (checkmark is AnimatedVectorDrawableCompat) {
            animatedVectorDrawableCompat = checkmark
            animatedVectorDrawableCompat.start()
        }
    }
}