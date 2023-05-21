package com.daisysoft.mysfu

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.AppViewModel
import com.daisysoft.mysfu.databinding.ActivityMainBinding
import com.daisysoft.mysfu.databinding.ActivitySplashBinding
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stateVM = ViewModelProvider(this)[AppViewModel::class.java]

        binding.floatingActionButton.setOnClickListener {
            revealActivity(it)
            stateVM.flagShowSplash()
        }
    }

    private fun revealActivity(view: View) {
        // TODO
        finish()
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}