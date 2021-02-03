package com.example.misohe.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.misohe.myProfile.ProfileResponseModel
import com.google.gson.Gson
import io.reactivex.subjects.PublishSubject

class LiveSharedPreference constructor(private val preferences: SharedPreferences) :
    LiveData<ProfileResponseModel>() {

    private val publisher = PublishSubject.create<String>()
    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == SharedPreferenceManager.PROFILE)
                value = Gson().fromJson(
                    preferences.getString(
                        SharedPreferenceManager.PROFILE, "no"
                    ), ProfileResponseModel::class.java
                )
        }

    override fun onActive() {
        super.onActive()

        value = Gson().fromJson(
            preferences.getString(
                SharedPreferenceManager.PROFILE, "no"
            ), ProfileResponseModel::class.java
        )
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}

/* private val updates = publisher.doOnSubscribe {
     preferences.registerOnSharedPreferenceChangeListener(listener)
 }.doOnDispose {
     if (!publisher.hasObservers())
         preferences.unregisterOnSharedPreferenceChangeListener(listener)
 }

 fun getPreferences(): SharedPreferences {
     return preferences
 }

 fun getUserProfile(key: String): LivePreference<ProfileResponseModel> {
     return LivePreference(updates, preferences, key)
 }*/
