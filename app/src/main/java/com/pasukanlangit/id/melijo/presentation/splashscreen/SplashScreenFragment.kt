package com.pasukanlangit.id.melijo.presentation.splashscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.databinding.FragmentSplashScreenBinding
import com.pasukanlangit.id.melijo.presentation.safeNavigate

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val binding : FragmentSplashScreenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToDevelopmentKeyFragment()
            findNavController().safeNavigate(action)
        }
    }
}