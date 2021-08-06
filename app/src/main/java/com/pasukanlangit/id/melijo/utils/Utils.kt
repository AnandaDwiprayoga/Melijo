package com.pasukanlangit.id.melijo.utils
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pasukanlangit.id.melijo.R
import java.lang.Math.*
import kotlin.math.roundToInt

val PERMISSIONS_STORAGE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

object MyUtils {
    fun setToolbarGreen(window: Window, context: Context){
        window.statusBarColor = ContextCompat.getColor(context, R.color.teal_700)
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun getRandomDistance() : Double = (50 until 350).random().toDouble()

    fun getKmFromLatLong(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lng1
        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lng2
        val distanceInMeters: Float = loc1.distanceTo(loc2)
        return distanceInMeters / 1000
    }

    fun getKilometers(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val PI_RAD = PI / 180.0
        val phi1 = lat1 * PI_RAD
        val phi2 = lat2 * PI_RAD
        val lam1 = long1 * PI_RAD
        val lam2 = long2 * PI_RAD
        val valueDistance = 6371.01 * kotlin.math.acos(
            kotlin.math.sin(phi1) * kotlin.math.sin(phi2) + kotlin.math.cos(phi1) * kotlin.math.cos(phi2) * kotlin.math.cos(
                lam2 - lam1
            )
        )
        return (valueDistance * 100).roundToInt() / 100.0

    }

}