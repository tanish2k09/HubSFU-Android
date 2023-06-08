package com.daisysoft.mysfu.ui.activity

import android.animation.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.daisysoft.AppViewModel
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.databinding.ActivityLoginBinding
import com.daisysoft.mysfu.ui.components.TransparentActivity

import com.daisysoft.mysfu.ui.fragment.login.LoginViewModel
import com.daisysoft.mysfu.ui.fragment.login.LoginViewModelFactory
import kotlin.math.max

class LoginActivity : TransparentActivity() {

    private lateinit var appVM: AppViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val splashTransitionResult = registerForColorResult {
        transitionFromSplash(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appVM = ViewModelProvider(this)[AppViewModel::class.java]
        splashFlow()

        // TODO: Remove this.
//        proceedWithTransition()

        val username = binding.username
        val password = binding.password
        val login = binding.login

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        login.isEnabled = true
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
                return@Observer
            }

            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            proceedWithTransition()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        fitWithinSystemBars(binding.newsletterAnimationView, 0, binding.login, resources.getDimensionPixelOffset(R.dimen.login_bottom_margin))
    }

    private fun splashFlow() {
        if (appVM.shouldShowSplash() || true) {

            splashTransitionResult.launch(Intent(this, SplashActivity::class.java))
            setupViewsForSplashTransition()
        }
    }

    private fun setupViewsForSplashTransition() {
        // Hide the login button text with overlay view
        // Make the login button centered
        // Make the login button full-screen
        val transitionView = binding.login

        transitionView.post {
            transitionView.visibility = View.VISIBLE
            transitionView.isEnabled = false
            transitionView.isClickable = false
            transitionView.setTextColor(getColor(android.R.color.transparent))
            val overlayCenterY = resources.displayMetrics.heightPixels / 2 - transitionView.height / 2
            val fullScaleX = resources.displayMetrics.widthPixels * 1.5f / transitionView.width.toFloat()
            val fullScaleY = resources.displayMetrics.heightPixels * 1.5f / transitionView.height.toFloat()
            val fullScale = max(fullScaleX, fullScaleY)

            transitionView.translationY = overlayCenterY - transitionView.y
            transitionView.scaleX = fullScale
            transitionView.scaleY = fullScale
        }
    }

    private fun transitionFromSplash(splashColor: Int) {
        Log.d("LoginActivity", "transitionFromSplash")
        val transitionView = binding.login
        val scaleDownDuration = resources.getInteger(R.integer.splash_login_scale_duration).toLong()
        val translateDuration = resources.getInteger(R.integer.splash_login_translate_duration).toLong()
        val scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        val translateDelay = resources.getInteger(R.integer.splash_login_translate_delay).toLong()
        val scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        val translationYHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
        val shiftDuration = resources.getInteger(R.integer.splash_login_shift_duration).toLong()
        val textFadeDuration = resources.getInteger(R.integer.splash_login_text_fade_duration).toLong()
        val textFadeDelay = resources.getInteger(R.integer.splash_login_text_fade_delay).toLong()

        ObjectAnimator.ofPropertyValuesHolder(transitionView, scaleXHolder, scaleYHolder).apply {
            duration = scaleDownDuration
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        ObjectAnimator.ofPropertyValuesHolder(transitionView, translationYHolder).apply {
            duration = translateDuration
            interpolator = DecelerateInterpolator()
            startDelay = translateDelay
            start()
        }

        val disabledColor = resources.getColor(R.color.primary_disabled, theme)
        val originalBG = transitionView.backgroundTintList
        ValueAnimator.ofArgb(splashColor, disabledColor).apply {
            duration = shiftDuration
            interpolator = LinearInterpolator()
            addUpdateListener {
                transitionView.background.setTint(it.animatedValue as Int)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transitionView.isClickable = true
                    transitionView.background.setTintList(originalBG)
                }
            })
            start()
        }
        ValueAnimator.ofArgb(getColor(android.R.color.transparent), getColor(android.R.color.white)).apply {
            duration = textFadeDuration
            startDelay = textFadeDelay
            addUpdateListener {
                transitionView.setTextColor(it.animatedValue as Int)
            }
            start()
        }

    }

    private fun loginShakeAnimation() {
        binding.login.isClickable = false
        val shakeDuration = resources.getInteger(R.integer.login_button_fail_shake_duration).toLong()
        val resetDuration = resources.getInteger(R.integer.login_button_fail_reset_duration).toLong()
        val shakeXHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        val scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1f)
        val scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.1f)
        ObjectAnimator
            .ofPropertyValuesHolder(binding.login, shakeXHolder, scaleXHolder, scaleYHolder)
            .setDuration(shakeDuration)
            .start();

        val resetAnimatorSet = AnimatorSet()
        resetAnimatorSet.apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.login, "scaleX", 1f),
                ObjectAnimator.ofFloat(binding.login, "scaleY", 1f)
            )
            interpolator = DecelerateInterpolator()
            duration = resetDuration
            startDelay = shakeDuration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.login.isClickable = true
                }
            })
            start()
        }
    }
    private fun showLoginFailed(@StringRes errorString: Int) {
        // Hide the keyboard
        getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
            (it as InputMethodManager).hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        // Shake the login button
        loginShakeAnimation()
    }

    private fun proceed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun proceedWithTransition() {
        binding.login.isClickable = false

        for (view in listOf(binding.newsletterAnimationView, binding.greetingText, binding.username, binding.password)) {
            AnimatorInflater.loadAnimator(this, R.animator.fade_up_animator).apply {
                setTarget(view)
                start()
            }
        }

        // Fade the text
        // Make the button a circle
        // Shrink it while moving it down to the bottom edge of the screen
        val transitionView = binding.login
        val textFadeDuration = resources.getInteger(R.integer.main_login_button_fade_duration).toLong()
        val shrinkSize = resources.getDimensionPixelSize(R.dimen.main_login_shrink_size)
        val shrinkDuration = resources.getInteger(R.integer.main_login_button_shrink_duration).toLong()
        val fillDuration = resources.getInteger(R.integer.main_login_button_fill_duration).toLong()
        val fillDelay = resources.getInteger(R.integer.main_login_button_fill_delay).toLong()
        val shiftDuration = resources.getInteger(R.integer.main_login_button_translate_duration).toLong()

        ValueAnimator.ofArgb(getColor(android.R.color.white), getColor(android.R.color.transparent)).apply {
            duration = textFadeDuration
            addUpdateListener {
                transitionView.setTextColor(it.animatedValue as Int)
            }
            start()
        }

        val typedVal = TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, typedVal, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                typedVal.data,
                resources.displayMetrics
            )
        }

        AnimatorSet().apply {
            playTogether(
                ValueAnimator.ofInt(transitionView.width, shrinkSize).apply {
                    addUpdateListener {
                        transitionView.updateLayoutParams { width = it.animatedValue as Int }
                    }
                },
                ValueAnimator.ofInt(transitionView.height, shrinkSize).apply {
                    addUpdateListener {
                        transitionView.updateLayoutParams { height = it.animatedValue as Int }
                    }
                },
            )
            interpolator = DecelerateInterpolator()
            duration = shrinkDuration
            startDelay = textFadeDuration
            start()
        }

        val bottomInset = transitionView.rootWindowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars()).bottom
        val deviceWidth = resources.displayMetrics.widthPixels
        val finalHeight = bottomInset + actionBarHeight
        val translationFinal = resources.displayMetrics.heightPixels - transitionView.y + transitionView.height / 2 - actionBarHeight
        ObjectAnimator.ofFloat(transitionView, "translationY", translationFinal).apply {
            interpolator = DecelerateInterpolator()
            duration = shiftDuration
            startDelay = textFadeDuration
            start()
        }

        val redColor = getColor(R.color.primary_red)
        val navBarColor = getColor(R.color.primary_red_light)
        val startingCornerRadius = resources.getDimensionPixelSize(R.dimen.login_button_corner).toFloat()
        val finalCornerRadius = resources.getDimensionPixelSize(R.dimen.navbar_rounded_corner).toFloat()
        val transitionViewBg = GradientDrawable().apply {
            setColor(redColor)
            cornerRadius = startingCornerRadius
        }
        transitionView.background = transitionViewBg
        AnimatorSet().apply {
            playTogether(
                ValueAnimator.ofInt(transitionView.width, deviceWidth).apply {
                    addUpdateListener {
                        transitionView.updateLayoutParams { width = it.animatedValue as Int }
                    }
                },
                ValueAnimator.ofInt(transitionView.height, finalHeight).apply {
                    addUpdateListener {
                        transitionView.updateLayoutParams { height = it.animatedValue as Int }
                    }
                },
                ValueAnimator.ofArgb(redColor, navBarColor).apply {
                    addUpdateListener {
                        transitionView.background.setTint(it.animatedValue as Int)
                    }
                },
                ValueAnimator.ofFloat(startingCornerRadius, finalCornerRadius).apply {
                    addUpdateListener {
                        val cornerRadius = it.animatedValue as Float
                        transitionViewBg.cornerRadii = floatArrayOf(
                            cornerRadius,
                            cornerRadius,
                            cornerRadius,
                            cornerRadius,
                            finalCornerRadius - cornerRadius,
                            finalCornerRadius - cornerRadius,
                            finalCornerRadius - cornerRadius,
                            finalCornerRadius - cornerRadius
                        )
                    }
                }
            )
            interpolator = DecelerateInterpolator()
            duration = fillDuration
            startDelay = textFadeDuration + shrinkDuration + fillDelay
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    proceed()
                    overridePendingTransition(0, android.R.anim.fade_out)
                    finish()
                }
            })
            start()
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}