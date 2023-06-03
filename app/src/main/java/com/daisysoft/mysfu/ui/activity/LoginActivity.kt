package com.daisysoft.mysfu.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.daisysoft.AppViewModel
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.databinding.ActivityLoginBinding
import com.daisysoft.mysfu.ui.components.TransparentActivity

import com.daisysoft.mysfu.ui.fragment.login.LoginViewModel
import com.daisysoft.mysfu.ui.fragment.login.LoginViewModelFactory
import com.daisysoft.mysfu.utils.StartActivityForTransitionContract
import kotlinx.coroutines.delay

class LoginActivity : TransparentActivity() {

    private lateinit var appVM: AppViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val splashTransitionResult = registerForActivityResult(StartActivityForTransitionContract()) {
        transitionFromSplash()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appVM = ViewModelProvider(this)[AppViewModel::class.java]
        splashFlow()

        // TODO: Remove this.
//        proceed()

        val username = binding.username
        val password = binding.password
        val login = binding.login

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

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
            proceed()
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
        fitBottomWithinSystemBars(binding.loginTransitionOverlay, resources.getDimensionPixelOffset(R.dimen.login_bottom_margin))
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

            transitionView.translationY = overlayCenterY - transitionView.y
            transitionView.scaleX = fullScaleX
            transitionView.scaleY = fullScaleY
        }
    }

    private fun transitionFromSplash() {
        Log.d("LoginActivity", "transitionFromSplash")
        val transitionView = binding.login
        val scaleDownDuration = resources.getInteger(R.integer.splash_login_scale_duration).toLong()
        val translateDuration = resources.getInteger(R.integer.splash_login_translate_duration).toLong()
        val scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        val translateDelay = resources.getInteger(R.integer.splash_login_translate_delay).toLong()
        val scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        val translationYHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
        val shiftDuration = resources.getInteger(R.integer.splash_login_shift_duration).toLong()
        val shiftDelay = resources.getInteger(R.integer.splash_login_shift_delay).toLong()
        val textFadeDuration = resources.getInteger(R.integer.splash_login_text_fade_duration).toLong()
        val textFadeDelay = resources.getInteger(R.integer.splash_login_text_fade_delay).toLong()

        ObjectAnimator.ofPropertyValuesHolder(transitionView, scaleXHolder, scaleYHolder).apply {
            duration = scaleDownDuration
            interpolator = DecelerateInterpolator()
            start()
        }

        ObjectAnimator.ofPropertyValuesHolder(transitionView, translationYHolder).apply {
            duration = translateDuration
            interpolator = DecelerateInterpolator()
            startDelay = translateDelay
            start()
        }

        val redColor = resources.getColor(R.color.primary_red, theme)
        val disabledColor = resources.getColor(R.color.primary_disabled, theme)
        val originalBG = transitionView.backgroundTintList
        ValueAnimator.ofArgb(redColor, disabledColor).apply {
            duration = shiftDuration
            addUpdateListener {
                transitionView.background.setTint(it.animatedValue as Int)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transitionView.isClickable = true
                    transitionView.background.setTintList(originalBG)
                }
            })
            startDelay = shiftDelay
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

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun proceed() {
        startActivity(Intent(this, MainActivity::class.java))
//        finish()
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