package com.pasukanlangit.id.melijo.presentation.main.home.seller

import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils.getBitmapFromDrawable
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentHomeSellerBinding
import com.pasukanlangit.id.melijo.presentation.main.MainViewModel
import com.pasukanlangit.id.melijo.presentation.main.home.HomeFragment
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SellerHomeFragment : Fragment(R.layout.fragment_home_seller){

    private val binding: FragmentHomeSellerBinding by viewBinding()
    private var accessToken : String ?= null
    private var canScroll = true
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mapboxMap : MapboxMap
    private lateinit var sellersAdapter: SellersAdapter
    private lateinit var permissionsManager: PermissionsManager

    private val ID_ICON_SELLER = "ID_ICON_SELLER"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accessToken = viewModel.getAccessToken()

        sellersAdapter = SellersAdapter()
        setUpRecyclerlView()

        binding.mapsview.onCreate(savedInstanceState)
        binding.mapsview.getMapAsync { mapBoxResult ->
            mapboxMap = mapBoxResult
            observeListSupplier()
        }
        binding.iconLock.setOnClickListener {
            canScroll = !canScroll

            if(!canScroll){
                binding.iconLock.setImageResource(R.drawable.icon_locked)
            }else{
                binding.iconLock.setImageResource(R.drawable.icon_lock)
            }
            (parentFragment as HomeFragment).setUserCanScroll(canScroll)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observeListSupplier() {
        accessToken?.let { token ->
            viewModel.getListSeller(token).observe(viewLifecycleOwner){
                binding.loading.isVisible = it is MyResponse.Loading
                if(it is MyResponse.Success){
                    mapboxMap.setStyle(Style.MAPBOX_STREETS){ style ->
                        showMyLocation(style)
                        activity?.getDrawable(R.drawable.icon_seller_png)?.let { iconSeller ->
                            val bitmapIconSeller = getBitmapFromDrawable(iconSeller)
                            val smallIconSeller = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmapIconSeller!!, 30, 30, true))

                            style.addImage(ID_ICON_SELLER, smallIconSeller)
                        }
//                        val latLongBoundsBuilder = LatLngBounds.Builder()
                        val symbolManager = SymbolManager(binding.mapsview, mapboxMap, style)
                        symbolManager.iconAllowOverlap = true

                        val options = ArrayList<SymbolOptions>()

                        it.data?.result?.let { sellers ->
                            sellersAdapter.submitList(sellers)
                            sellers.forEach{ dataSeller ->
                                options.add(
                                    SymbolOptions()
                                        .withLatLng(
                                            LatLng(
                                                dataSeller.latitude,
                                                dataSeller.longitude
                                            )
                                        )
                                        .withIconImage(ID_ICON_SELLER)
                                )
                            }
//                             latLongBoundsBuilder.include(LatLng(dataSeller.latitude, dataSeller.longitude))

                        }
                        symbolManager.create(options)
//                        val latLongBounds = latLongBoundsBuilder.build()
//                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLongBounds,50),5000)
//                        try {
//                        }catch (e:Exception){
//                            Toast.makeText(requireContext(), "Seller kosong", Toast.LENGTH_SHORT).show()
//                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerlView() {
        binding.rvListSeller.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListSeller.adapter = sellersAdapter
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()
            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(requireContext(), style)
                .locationComponentOptions(locationComponentOptions)
                .build()
            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            val mylocation = LatLng(
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 12.0))
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        requireContext(),
                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    }

                }
            })
            permissionsManager.requestLocationPermissions(activity)
        }

    }

    override fun onStart() {
        super.onStart()
        binding.mapsview.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapsview.onResume()
    }
    override fun onPause() {
        super.onPause()
        binding.mapsview.onPause()
    }
    override fun onStop() {
        super.onStop()
        binding.mapsview.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapsview.onSaveInstanceState(outState)
    }

//    override fun onDestroy() {
//        binding.mapsview.onDestroy()
//        super.onDestroy()
//    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapsview.onLowMemory()
    }
}