package com.example.misobo.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.misobo.R
import com.example.misobo.onBoarding.models.User
import com.google.gson.Gson
import io.reactivex.Completable

private const val ONBOARDED = "has_onboarded"

object SharedPreferenceManager {

    private const val USER = "user"
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    fun getUser(): User? =
        Gson().fromJson(
            sharedPreferences?.getString(
                USER, null
            ), User::class.java
        )

    fun setUser(context: Context?, user: User?): Completable {
        context?.let {
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

    fun isOnBoarded(): Boolean =
        sharedPreferences != null && sharedPreferences?.getBoolean(
            ONBOARDED,
            false
        ) ?: false

    fun setOnBoarded(value: Boolean) = sharedPreferences?.edit().apply {
        this?.putBoolean(ONBOARDED,value)
        this?.apply()
    }

    fun clear() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}