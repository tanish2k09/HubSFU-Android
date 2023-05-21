package com.daisysoft

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.daisysoft.mysfu.data.constants.StringConstants

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private var _statePrefs =
        application.getSharedPreferences(
            StringConstants.SHARED_PREF_STATE_FILE.value,
            Context.MODE_PRIVATE
        )

    fun shouldShowSplash(): Boolean {
        return _statePrefs.getBoolean(
                StringConstants.STATE_SHOW_SPLASH.value,
                true
            )
    }

    fun flagShowSplash() {
        _statePrefs.edit().putBoolean(StringConstants.STATE_SHOW_SPLASH.value, false).commit()
    }
}