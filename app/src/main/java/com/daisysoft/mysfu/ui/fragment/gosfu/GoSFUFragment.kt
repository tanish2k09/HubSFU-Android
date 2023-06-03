package com.daisysoft.mysfu.ui.fragment.gosfu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daisysoft.mysfu.R

class GoSFUFragment : Fragment() {

    companion object {
        fun newInstance() = GoSFUFragment()
    }

    private lateinit var viewModel: GoSFUViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gosfu, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GoSFUViewModel::class.java)
        // TODO: Use the ViewModel
    }

}