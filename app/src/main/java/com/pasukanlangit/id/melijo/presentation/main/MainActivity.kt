package com.pasukanlangit.id.melijo.presentation.main

import android.annotation.SuppressLint
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateLocationRequest
import com.pasukanlangit.id.melijo.databinding.ActivityMainBinding
import com.pasukanlangit.id.melijo.utils.MyResponse
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private lateinit var permissionsManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        MyUtils.setToolbarGreen(window, this)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainFragContainer.id) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.btmNav.setupWithNavController(navController)

        getCurrentLocation()
    }



    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationManager =
                this.getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val bestProvider =
                java.lang.String.valueOf(locationManager.getBestProvider(criteria, true)).toString()

            val location: Location? = locationManager.getLastKnownLocation(bestProvider)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                viewModel.updateLocation(UpdateLocationRequest(latitude, longitude)).observe(this){
                    if (it is MyResponse.Success) {
                        viewModel.saveLocationUser(UpdateLocationRequest(latitude, longitude))
                    }
                }
            } else {
                //This is what you need:
                locationManager.requestLocationUpdates(
                    bestProvider,
                    1000.toLong(),
                    0f,
                    this@MainActivity as LocationListener
                )
            }
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        this@MainActivity,
                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        getCurrentLocation()
                    }

                }
            })
            permissionsManager.requestLocationPermissions(this)
        }
    }
}