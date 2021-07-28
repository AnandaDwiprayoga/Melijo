package com.pasukanlangit.id.melijo.presentation.mainprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityMainProviderBinding
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

class MainProviderActivity : AppCompatActivity(R.layout.activity_main_provider) {

    private val binding: ActivityMainProviderBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainFragContainer.id) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.btmNav.setupWithNavController(navController)
    }
}