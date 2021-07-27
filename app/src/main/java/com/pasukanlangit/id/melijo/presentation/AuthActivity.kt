package com.pasukanlangit.id.melijo.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.pasukanlangit.id.melijo.R
import com.pasukanlangit.id.melijo.presentation.auth.UserType
import com.pasukanlangit.id.melijo.presentation.main.MainActivity
import com.pasukanlangit.id.melijo.presentation.mainprovider.MainProviderActivity
import dagger.hilt.android.AndroidEntryPoint

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes id: Int,
    args: Bundle? = null
) {
    if (currentDestinationId == currentDestination?.id) {
        navigate(id, args)
    }
}

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    fun navigateToMain(accountType: String?) {
        finishAffinity()
        if(accountType == UserType.TYPE_BUYER.value){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, MainProviderActivity::class.java))
        }
    }
}