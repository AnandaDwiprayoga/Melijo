package com.pasukanlangit.id.melijo.utils
import android.content.Context
import android.view.Window
import androidx.core.content.ContextCompat
import com.pasukanlangit.id.melijo.R

object MyUtils {
    fun setToolbarGreen(window: Window, context: Context){
        window.statusBarColor = ContextCompat.getColor(context, R.color.teal_700)
    }
}