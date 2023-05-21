package com.daisysoft.mysfu.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.mysfu.data.constants.EventData
import com.daisysoft.mysfu.data.constants.RestaurantData
import com.daisysoft.mysfu.databinding.FragmentCommunityBinding
import com.daisysoft.mysfu.ui.components.EventFragment
import com.daisysoft.mysfu.ui.components.RestaurantFragment
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
            RestaurantData.restaurants.forEachIndexed { index, _ ->
                add<RestaurantFragment>(binding.restaurantsContainer.id, args = bundleOf("index" to index))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}