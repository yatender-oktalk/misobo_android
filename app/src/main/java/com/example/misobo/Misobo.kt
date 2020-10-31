package com.example.misobo

import android.app.Application
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.Stetho

class Misobo : Application() {

    companion object {
        lateinit var instance: Misobo
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(this)
        Stetho.initializeWithDefaults(this)
    }



}