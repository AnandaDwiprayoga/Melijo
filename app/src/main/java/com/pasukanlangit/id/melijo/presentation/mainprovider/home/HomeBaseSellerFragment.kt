package com.pasukanlangit.id.melijo.presentation.mainprovider.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
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
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.data.network.model.response.LoginProducerData
import com.pasukanlangit.id.melijo.databinding.FragmentHomeBaseSellerBinding
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderViewModel
import com.pasukanlangit.id.melijo.presentation.mainprovider.home.homebaseseller.ProductBaseSellerAdapter
import com.pasukanlangit.id.melijo.utils.MyResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBaseSellerFragment : Fragment(R.layout.fragment_home_base_seller) {

    private lateinit var mAdapter: ProductBaseSellerAdapter
    private val binding: FragmentHomeBaseSellerBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()
    private lateinit var mapboxMap : MapboxMap

    private lateinit var permissionsManager: PermissionsManager

    private val ID_ICON_SELLER = "ID_ICON_SELLER"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRvProduct()
        observeProfileProducer()
        observeProductProvider()

        with(binding){
            viewModel.getAllUserForProducer()
            viewModel.getAllProductsProvider()

            mapsview.onCreate(savedInstanceState)
            mapsview.getMapAsync { mapBoxResult ->
                mapboxMap = mapBoxResult
                observeListSeller()
            }

            btnAtur.setOnClickListener {
                startActivity(Intent(requireContext(),ManageProducerActivity::class.java))
            }

            labeledSwitch.setOnClickListener {
                viewModel.toggleStatus().observe(viewLifecycleOwner){
                    binding.loading.isVisible = it is MyResponse.Loading
                    when(it){
                        is MyResponse.Success -> {
                            it.data?.result?.data?.let { profile ->
                                labeledSwitch.isOn = profile.isActive
                            }
                        }
                        is MyResponse.Loading -> { }
                        is MyResponse.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setUpRvProduct() {
        mAdapter = ProductBaseSellerAdapter()
        with(binding.rvProduct){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    private fun observeProductProvider() {
        viewModel.productProvider.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                    it.data?.result?.let { products ->
                        mAdapter.submitList(products)
                    }
                }
                is MyResponse.Loading -> { }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeProfileProducer() {
        viewModel.profileProducer.observe(viewLifecycleOwner){
            binding.loading.isVisible = it is MyResponse.Loading
            when(it){
                is MyResponse.Success -> {
                   it.data?.result?.let { profile ->
                       setUpUI(profile)
                   }
                }
                is MyResponse.Loading -> { }
                is MyResponse.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpUI(profile: LoginProducerData) {
        with(binding){
            Glide.with(requireContext())
                .load(profile.photo)
                .circleCrop()
                .into(ivSeller)

            tvNameSeller.text = profile.name
            tvRating.text = profile.rating.toString()
            labeledSwitch.isOn = profile.isActive
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observeListSeller() {
        viewModel.allUsers.observe(viewLifecycleOwner){
            binding.loadingMaps.isVisible = it is MyResponse.Loading
            if(it is MyResponse.Success){
                mapboxMap.setStyle(Style.MAPBOX_STREETS){ style ->
                    showMyLocation(style)
                    activity?.getDrawable(R.drawable.ic_icon_buyer)?.let { iconSeller ->
                        val bitmapIconSeller = BitmapUtils.getBitmapFromDrawable(iconSeller)
                        val smallIconSeller = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmapIconSeller!!, 60, 60, true))

                        style.addImage(ID_ICON_SELLER, smallIconSeller)
                    }
//                        val latLongBoundsBuilder = LatLngBounds.Builder()
                    val symbolManager = SymbolManager(binding.mapsview, mapboxMap, style)
                    symbolManager.iconAllowOverlap = true

                    val options = ArrayList<SymbolOptions>()

                    it.data?.result?.let { users ->
                        users.forEach{ dataUser ->
                            options.add(
                                SymbolOptions()
                                    .withLatLng(
                                        LatLng(
                                            dataUser.latitude.toDouble(),
                                            dataUser.longitude.toDouble()
                                        )
                                    )
                                    .withIconImage(ID_ICON_SELLER)
                            )
                        }

                    }
                    symbolManager.create(options)
                }
            }
        }
    }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .foregroundDrawable(R.drawable.icon_seller_small)
                .backgroundDrawable(R.drawable.icon_seller_small)
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