package com.daisysoft.mysfu.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.constants.ClubEventsData
import com.daisysoft.mysfu.data.constants.CourseData
import com.daisysoft.mysfu.databinding.FragmentCourseCardBinding
import com.daisysoft.mysfu.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CourseCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseCardFragment : Fragment() {

    private var _binding: FragmentCourseCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCourseCardBinding.inflate(inflater, container, false)

        val courseInfoIndex = requireArguments().getInt("index")

        val courseInfo = if (requireArguments().getBoolean("club")) {
            ClubEventsData.events[courseInfoIndex]
        } else {
            CourseData.courses[courseInfoIndex]
        }

        binding.courseIcon.setImageDrawable(resources.getDrawable(courseInfo.icon, requireContext().theme))
        binding.courseName.text = courseInfo.name
        binding.courseTitle.text = courseInfo.title
        binding.courseTimeText.text = courseInfo.time
        binding.courseLocationText.text = courseInfo.location
        binding.courseProfText.text = courseInfo.instructor

        if (courseInfo.wqb != null) {
            binding.courseWqb.text = courseInfo.wqb
        } else {
            binding.courseWqb.visibility = View.INVISIBLE
        }

        return binding.root
    }

}