package com.daisysoft.mysfu.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.activity.result.contract.ActivityResultContract

class StartActivityForTransitionContract: ActivityResultContract<Intent, Int>() {

    companion object {
        const val LAST_COLOR_KEY = "last_color"
        const val DEFAULT_LAST_COLOR = Color.WHITE
    }
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.extras?.getInt(LAST_COLOR_KEY, DEFAULT_LAST_COLOR)?: DEFAULT_LAST_COLOR
    }

}