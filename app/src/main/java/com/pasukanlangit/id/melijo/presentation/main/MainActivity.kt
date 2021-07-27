package com.pasukanlangit.id.melijo.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mapbox.mapboxsdk.Mapbox
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityMainBinding
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        MyUtils.setToolbarGreen(window, this)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainFragContainer.id) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.btmNav.setupWithNavController(navController)
    }
}