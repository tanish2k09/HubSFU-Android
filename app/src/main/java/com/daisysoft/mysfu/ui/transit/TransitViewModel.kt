package com.daisysoft.mysfu.ui.transit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransitViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is transit Fragment"
    }
    val text: LiveData<String> = _text
}