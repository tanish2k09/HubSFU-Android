package com.daisysoft.mysfu.ui.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.constants.EventData
import com.daisysoft.mysfu.databinding.FragmentCourseCardBinding
import com.daisysoft.mysfu.databinding.FragmentEventBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEventBinding.inflate(inflater, container, false)

        val eventInfoIndex = requireArguments().getInt("index")
        val eventInfo = EventData.events[eventInfoIndex]

        binding.eventBanner.setImageDrawable(resources.getDrawable(eventInfo.banner, requireContext().theme))
        binding.eventTitle.text = eventInfo.title
        binding.eventDate.text = eventInfo.date
        binding.eventDepartment.text = eventInfo.department
        binding.eventPresenter.text = eventInfo.presenter
        binding.eventSchedule.text = eventInfo.schedule

        if (eventInfo.location != null) {
            binding.eventLocation.text = eventInfo.location
        } else {
            binding.eventLocation.visibility = View.INVISIBLE
        }

        return binding.root
    }
}