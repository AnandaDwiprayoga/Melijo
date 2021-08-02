package com.pasukanlangit.id.melijo.presentation.mainprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.ActivityMainProviderBinding
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.utils.MyUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainProviderActivity : AppCompatActivity(R.layout.activity_main_provider) {

    private val binding: ActivityMainProviderBinding by viewBinding()
    private val viewModel: MainProviderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyUtils.setToolbarGreen(window, this)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainFragContainer.id) as NavHostFragment
        val navController = navHostFragment.findNavController()

        val accountType = viewModel.accountType

        accountType?.let { type ->
            if(type == UserType.TYPE_SUPPLIER.value){
                navController.navigate(R.id.homeBaseSupplierFragment)
            }else{
                navController.navigate(R.id.homeBaseSellerFragment)
            }
        }

        binding.btmNav.setupWithNavController(navController)
        binding.btmNav.setOnNavigationItemSelectedListener {
            if(it.itemId == R.id.homeProviderFragment) {
                val accountType = viewModel.accountType
                accountType?.let { type ->
                    if(type == UserType.TYPE_SUPPLIER.value){
                        navController.navigate(R.id.homeBaseSupplierFragment)
                    }else{
                        navController.navigate(R.id.homeBaseSellerFragment)
                    }
                }
                true
            }else{
                it.onNavDestinationSelected(navController)
            }
        }
    }
}