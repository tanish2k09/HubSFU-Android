package com.daisysoft.mysfu.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class HomeViewModel : ViewModel() {

    private val _day = MutableLiveData<String>()
    val day: LiveData<String> = _day

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val calendar = Calendar.getInstance()

    fun updateDate() {
        _day.value = LocalDate.now().dayOfWeek.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        _date.value = SimpleDateFormat("MMM dd, yyyy").format(calendar.time)
    }
}