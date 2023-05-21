package com.daisysoft.mysfu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.mysfu.data.constants.CourseData
import com.daisysoft.mysfu.databinding.FragmentHomeBinding
import com.daisysoft.mysfu.ui.components.CourseCardFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            CourseData.courses.forEachIndexed { index, _ ->
                add<CourseCardFragment>(binding.coursesContainer.id, args = bundleOf("index" to index))
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        // TODO:
        // 1) Refresh date and weather in VM
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}