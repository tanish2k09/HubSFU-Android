package com.daisysoft.mysfu.ui.activity

import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.AppViewModel
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.databinding.ActivitySplashBinding
import com.daisysoft.mysfu.ui.components.TransparentActivity
import kotlin.math.hypot

class SplashActivity : TransparentActivity() {

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

        fitWithinSystemBars(binding.welcomeText,
            resources.getDimensionPixelOffset(R.dimen.splash_margin_top), binding.tapText,
            resources.getDimensionPixelOffset(R.dimen.splash_margin_bottom)
        )
    }

    private fun revealActivity(view: View) {
        // Pause the lottie animation for efficiency and make FAB un-clickable
        binding.animationView.pauseAnimation()
        binding.floatingActionButton.isClickable = false

        // Get durations from resources
        val translateDuration = resources.getInteger(R.integer.fab_ripple_translate_duration).toLong()
        val iconFadeDuration = resources.getInteger(R.integer.fab_ripple_icon_fade_duration).toLong()
        val scaleDownDuration = resources.getInteger(R.integer.fab_ripple_scale_down_duration).toLong()
        val explodeDuration = resources.getInteger(R.integer.fab_ripple_explode_duration).toLong()

        // Fade the FAB icon by changing transparency to 0
        ObjectAnimator.ofArgb(binding.floatingActionButton.drawable, "alpha", 0).apply {
            duration = iconFadeDuration
            interpolator = AccelerateInterpolator()
            start()
        }

        // Move the FAB to the center of the screen
        val displayCenter = resources.displayMetrics.heightPixels / 2 - binding.floatingActionButton.height / 2
        ObjectAnimator.ofFloat(binding.floatingActionButton, View.Y, displayCenter.toFloat()).apply {
            duration = translateDuration
            interpolator = DecelerateInterpolator()
            startDelay = iconFadeDuration
            start()
        }


        // Shrink and then explode the FAB to fill the screen
        val displayHypo = hypot(resources.displayMetrics.widthPixels.toDouble(), resources.displayMetrics.heightPixels.toDouble()).toFloat() / 1.5f
        val fabScaleUp = displayHypo * 2 / binding.floatingActionButton.height
        val fabScaleDownFactor = resources.getInteger(R.integer.fab_ripple_scale_down_factor).toFloat()

        val shrinkXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f / fabScaleDownFactor)
        val shrinkYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f / fabScaleDownFactor)
        ObjectAnimator.ofPropertyValuesHolder(binding.floatingActionButton, shrinkXHolder, shrinkYHolder).apply {
            duration = scaleDownDuration
            interpolator = DecelerateInterpolator()
            start()
        }

        val explodeXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, fabScaleUp)
        val explodeYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, fabScaleUp)
        ObjectAnimator.ofPropertyValuesHolder(binding.floatingActionButton, explodeXHolder, explodeYHolder).apply {
            duration = explodeDuration
            interpolator = AccelerateInterpolator()
            startDelay = scaleDownDuration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    this@SplashActivity.finish()
                    overridePendingTransition(0, 0)
                }
            })
            start()
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}