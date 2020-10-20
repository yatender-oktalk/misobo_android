package com.example.misobo

import android.app.Application
import com.facebook.stetho.Stetho

class Misobo : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }


}