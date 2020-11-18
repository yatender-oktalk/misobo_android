package com.example.misobo

import android.content.Context
import android.content.SharedPreferences
import com.example.misobo.onBoarding.models.User
import com.google.gson.Gson
import io.reactivex.Completable

object SharedPreferenceManager {

    private const val USER = "user"
    private var prefName: String? = null

    private fun initializePreferences(context: Context?): SharedPreferences? {
        prefName = context?.getString(R.string.app_name)
        return context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    fun getUser(context: Context?): User? {
        val sharedPreferences = initializePreferences(context)
        return Gson().fromJson(sharedPreferences?.getString(USER, null), User::class.java)
    }

    fun setUser(context: Context?, user: User?): Completable {
        context?.let {
            val sharedPreferences = initializePreferences(context)
            sharedPreferences?.edit()?.apply {
                if (user == null) {
                    putString(USER, null)
                } else {
                    putString(USER, Gson().toJson(user))
                }
                apply()
            }
        }
        return Completable.complete()
    }
}