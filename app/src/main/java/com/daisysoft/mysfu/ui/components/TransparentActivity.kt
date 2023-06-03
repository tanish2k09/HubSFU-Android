package com.daisysoft.mysfu.ui.components

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.daisysoft.mysfu.utils.StartActivityForTransitionContract

open class TransparentActivity: AppCompatActivity() {

    protected fun fitWithinSystemBars(topView: View, marginTop: Int, bottomView: View, marginBottom: Int) {
        fitTopWithinSystemBars(topView, marginTop)
        fitBottomWithinSystemBars(bottomView, marginBottom)
    }

    protected fun fitTopWithinSystemBars(topView: View, marginTop: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(topView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.top + marginTop
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun fitBottomWithinSystemBars(bottomView: View, marginBottom: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(bottomView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = insets.bottom + marginBottom
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun registerColorResult(callback: () -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(StartActivityForTransitionContract()) { callback() }
    }

}