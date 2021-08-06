package com.pasukanlangit.id.melijo.data.sharedpref

import android.content.SharedPreferences
import com.google.gson.Gson
import com.pasukanlangit.id.melijo.data.network.model.request.UpdateLocationRequest
import javax.inject.Inject


class AuthSharedPref @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val KEY_ACCESS_TOKEN : String = "KEY_ACCESS_TOKEN"
        const val KEY_ACCOUNT_TYPE : String = "KEY_ACCOUNT_TYPE"
        const val KEY_ACCOUNT_LOCATION : String = "KEY_ACCOUNT_LOCATION"
        const val KEY_SAVE_SESSION : String = "KEY_SAVE_SESSION"
        const val KEY_IMAGE_USER : String = "KEY_IMAGE_USER"
        const val KEY_NAME_USER : String = "KEY_NAME_USER"
    }


    fun getAccessToken(): String {
        return "Bearer ${sharedPreferences.getString(KEY_ACCESS_TOKEN, null)}"
    }

    fun setAccessToken(value: String)  {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()
    }

    fun getIsSaveSession(): Boolean {
        return sharedPreferences.getBoolean(KEY_SAVE_SESSION, false)
    }

    fun setLocationUser(location: UpdateLocationRequest)  {
        val locationString = Gson().toJson(location)
        sharedPreferences.edit().putString(KEY_ACCOUNT_LOCATION, locationString).apply()
    }

    fun getLocationUser(): UpdateLocationRequest? {
        val json: String? = sharedPreferences.getString(KEY_ACCOUNT_LOCATION, null)
        return Gson().fromJson(json, UpdateLocationRequest::class.java)
    }

    fun setImageUser(value: String)  {
        sharedPreferences.edit().putString(KEY_IMAGE_USER, value).apply()
    }

    fun getImageUser(): String? {
        return sharedPreferences.getString(KEY_IMAGE_USER, null)
    }


    fun setNameUser(value: String)  {
        sharedPreferences.edit().putString(KEY_NAME_USER, value).apply()
    }

    fun getNameUser(): String? {
        return sharedPreferences.getString(KEY_NAME_USER, null)
    }

    fun setIsSaveSession(value: Boolean)  {
        sharedPreferences.edit().putBoolean(KEY_SAVE_SESSION, value).apply()
    }

    fun removeAccessToken()  {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply()
    }

    fun getAccountType(): String? {
        return sharedPreferences.getString(KEY_ACCOUNT_TYPE, null)
    }

    fun setAccountType(value: String)  {
        sharedPreferences.edit().putString(KEY_ACCOUNT_TYPE, value).apply()
    }
}