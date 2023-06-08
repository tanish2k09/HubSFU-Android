package com.daisysoft.mysfu.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.databinding.ActivityMainBinding
import com.daisysoft.mysfu.ui.components.TransparentActivity
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable


class MainActivity : TransparentActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radius = resources.getDimension(R.dimen.navbar_rounded_corner)
        val navView: BottomNavigationView = binding.navView
        val navBackground = navView.background as MaterialShapeDrawable
        navView.background.setTint(getColor(R.color.primary_red_light))
        window.navigationBarColor = getColor(R.color.primary_red_light)
        navBackground.shapeAppearanceModel = navBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .build()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
    }
}