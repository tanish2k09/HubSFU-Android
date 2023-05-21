package com.daisysoft.mysfu.ui.components

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.constants.RestaurantData
import com.daisysoft.mysfu.databinding.FragmentCourseCardBinding
import com.daisysoft.mysfu.databinding.FragmentRestaurantBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantFragment : Fragment() {

    private var _binding: FragmentRestaurantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)

        val restaurantInfoIndex = requireArguments().getInt("index")
        val restaurantInfo = RestaurantData.restaurants[restaurantInfoIndex]

        binding.restaurantName.text = restaurantInfo.name
        binding.restaurantLocationText.text = restaurantInfo.location
        binding.restaurantTimeText.text = restaurantInfo.hours
        binding.costText.text = restaurantInfo.cost
        binding.restaurantIcon.setImageDrawable(resources.getDrawable(restaurantInfo.logo, requireContext().theme))
        binding.tag.text = restaurantInfo.tag
        binding.restaurantCard.background.setTint(resources.getColor(restaurantInfo.color, requireContext().theme))


        return binding.root
    }
}