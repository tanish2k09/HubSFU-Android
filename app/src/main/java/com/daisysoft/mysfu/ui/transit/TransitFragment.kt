package com.daisysoft.mysfu.ui.transit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.databinding.FragmentTransitBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class TransitFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTransitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transitViewModel =
            ViewModelProvider(this)[TransitViewModel::class.java]

        _binding = FragmentTransitBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment: SupportMapFragment = binding.mapFragment.getFragment()
//        mapFragment.getMapAsync(this)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }
}