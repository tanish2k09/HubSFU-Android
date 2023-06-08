package com.daisysoft.mysfu.ui.fragment.community

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.mysfu.data.constants.ClubEventsData
import com.daisysoft.mysfu.data.constants.EventData
import com.daisysoft.mysfu.data.constants.RestaurantData
import com.daisysoft.mysfu.databinding.FragmentCommunityBinding
import com.daisysoft.mysfu.ui.components.CourseCardFragment
import com.daisysoft.mysfu.ui.components.EventFragment
import com.daisysoft.mysfu.ui.components.RestaurantFragment
import com.daisysoft.mysfu.ui.components.TransparentActivity
import io.kommunicate.KmChatBuilder
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KmCallback

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val communityViewModel =
            ViewModelProvider(this)[CommunityViewModel::class.java]

        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Kommunicate.init(activity?.applicationContext ?: requireContext(), "1f6c4de3c06ac14890ef56e72e18a11cc")

        binding.chatbot.setOnClickListener {
            KmConversationBuilder(requireContext())
                .setSingleConversation(false)
                .launchConversation(object : KmCallback {
                    override fun onSuccess(message: Any?) {

                    }

                    override fun onFailure(error: Any?) {
                        Toast.makeText(
                            requireContext(),
                            "Error: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            EventData.events.forEachIndexed { index, _ ->
                add<EventFragment>(binding.eventsContainer.id, args = bundleOf("index" to index))
            }
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            ClubEventsData.events.forEachIndexed { index, _ ->
                add<CourseCardFragment>(binding.clubContainer.id, args = bundleOf("index" to index, "club" to true))
            }
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            RestaurantData.restaurants.forEachIndexed { index, _ ->
                add<RestaurantFragment>(binding.restaurantsContainer.id, args = bundleOf("index" to index))
            }
        }

        if (activity is TransparentActivity) {

            val typedVal = TypedValue()
            (activity as TransparentActivity).apply {
                fitTopWithinSystemBars(binding.communityTopLayout, 0)

                if (requireContext().theme.resolveAttribute(
                        android.R.attr.actionBarSize,
                        typedVal,
                        true
                    )
                ) {
                    val inset = TypedValue.complexToDimensionPixelSize(
                        typedVal.data,
                        resources.displayMetrics
                    )

                    fitBottomWithinSystemBars(
                        binding.communityTopNestedScrollView,
                        inset,
                        consume = false
                    )

                    fitBottomWithinSystemBars(binding.chatbot, binding.chatbot.marginBottom + inset)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}