package com.daisysoft.mysfu.ui.fragment.home

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.mysfu.data.constants.CourseData
import com.daisysoft.mysfu.data.constants.EventData
import com.daisysoft.mysfu.databinding.FragmentHomeBinding
import com.daisysoft.mysfu.ui.components.CourseCardFragment
import com.daisysoft.mysfu.ui.components.EventFragment
import com.daisysoft.mysfu.ui.components.TransparentActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            CourseData.courses.forEachIndexed { index, _ ->
                add<CourseCardFragment>(binding.coursesContainer.id, args = bundleOf("index" to index))
            }
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            EventData.events.forEachIndexed { index, _ ->
                add<EventFragment>(binding.eventsContainer.id, args = bundleOf("index" to index))
            }
        }

        attachViewModelListeners()

        if (activity is TransparentActivity) {

            val typedVal = TypedValue()
            (activity as TransparentActivity).apply {
                fitTopWithinSystemBars(binding.homeTopLayoutContainer, 0)

                if (requireContext().theme.resolveAttribute(
                        android.R.attr.actionBarSize,
                        typedVal,
                        true
                    )
                ) {
                    fitBottomWithinSystemBars(
                        binding.homeTopNestedScrollView,
                        TypedValue.complexToDimensionPixelSize(
                            typedVal.data,
                            resources.displayMetrics
                        ),
                        consume = false
                    )
                }
            }
        }
        return root
    }

    private fun attachViewModelListeners() {
        homeViewModel.day.observe(viewLifecycleOwner) { day ->
            binding.dayText.text = day
        }

        homeViewModel.date.observe(viewLifecycleOwner) { date ->
            binding.dateText.text = date
        }
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.updateDate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}