package com.example.misobo.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.misobo.R
import com.example.misobo.myProfile.ProfileResponseModel
import com.example.misobo.onBoarding.models.User
import com.google.gson.Gson
import io.reactivex.Completable

private const val ONBOARDED = "has_onboarded"
private const val IS_MIND_UNLOCKED = "is_mind_unlocked"
private const val IS_BODY_UNLOCKED = "is_body_unlocked"

object SharedPreferenceManager {

    const val USER = "user"
    const val PROFILE = "profile"
    const val NAME = "name"
    const val PROFILE_IMAGE = "profileImage"

    var sharedPreferences: SharedPreferences? = null

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

    fun setUser(user: User?): Completable {
        sharedPreferences?.edit()?.apply {
            if (user == null) {
                putString(USER, null)
            } else {
                putString(USER, Gson().toJson(user))
            }
            apply()
        }
        return Completable.complete()
    }

    fun setUserProfile(profile: ProfileResponseModel): Completable {
        sharedPreferences?.edit()?.apply {
            if (profile != null) {
                putString(PROFILE, Gson().toJson(profile))
            }
            apply()
        }
        return Completable.complete()
    }

    fun getUserProfile(): ProfileResponseModel? = Gson().fromJson(
        sharedPreferences?.getString(
            PROFILE, null
        ), ProfileResponseModel::class.java
    )

    fun setName(name: String): Completable {
        sharedPreferences?.edit()?.apply {
            if (name != null) {
                putString(NAME, name)
            }
            apply()
        }
        return Completable.complete()
    }

    fun getName(): String? = sharedPreferences?.getString(
        NAME, null
    )

    fun setProfileImage(uri: String): Completable {
        sharedPreferences?.edit()?.apply {
            if (uri != null) {
                putString(PROFILE_IMAGE, uri)
            }
            apply()
        }
        return Completable.complete()
    }

    fun getProfileImage(): String? = sharedPreferences?.getString(
        PROFILE_IMAGE, null
    )

    fun isOnBoarded(): Boolean =
        sharedPreferences != null && sharedPreferences?.getBoolean(
            ONBOARDED,
            false
        ) ?: false

    fun setOnBoarded(value: Boolean) = sharedPreferences?.edit().apply {
        this?.putBoolean(ONBOARDED, value)
        this?.apply()
    }

    fun setMindUnlock(isMindUnlocked: Boolean) =
        sharedPreferences?.edit().apply {
            this?.putBoolean(IS_MIND_UNLOCKED, isMindUnlocked)
            this?.apply()
        }

    fun setBodyUnlock(isBodyUnlock: Boolean) =
        sharedPreferences?.edit().apply {
            this?.putBoolean(IS_BODY_UNLOCKED, isBodyUnlock)
            this?.apply()
        }

    fun isMindUnlocked(): Boolean = sharedPreferences != null && sharedPreferences?.getBoolean(
        IS_MIND_UNLOCKED, false
    ) ?: false

    fun isBodyUnlocked(): Boolean = sharedPreferences != null && sharedPreferences?.getBoolean(
        IS_BODY_UNLOCKED, false
    ) ?: false

    fun clear() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}