package com.daisysoft.mysfu.ui.components

import android.content.Intent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.daisysoft.mysfu.utils.StartActivityForTransitionContract

open class TransparentActivity: AppCompatActivity() {

    fun fitWithinSystemBars(topView: View, marginTop: Int, bottomView: View, marginBottom: Int) {
        fitTopWithinSystemBars(topView, marginTop)
        fitBottomWithinSystemBars(bottomView, marginBottom)
    }

    fun fitTopWithinSystemBars(topView: View, marginTop: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(topView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.top + marginTop
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    fun fitBottomWithinSystemBars(bottomView: View, marginBottom: Int, consume: Boolean = true) {
        ViewCompat.setOnApplyWindowInsetsListener(bottomView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = insets.bottom + marginBottom
            }

            if (consume) {
                WindowInsetsCompat.CONSUMED
            } else {
                windowInsets
            }
        }
    }

    fun padTopWithinSystemBars(topView: View, padTop: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(topView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top + padTop)

            WindowInsetsCompat.CONSUMED
        }
    }

    fun padBottomWithinSystemBars(bottomView: View, padBottom: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(bottomView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom + padBottom)

            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun registerForColorResult(callback: (it: Int) -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(StartActivityForTransitionContract()) {
            callback(it)
        }
    }

}