package com.example.misobo

import android.app.Application
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.myProfile.ProfileResponseAction
import com.example.misobo.myProfile.ProfileService
import com.example.misobo.onBoarding.api.OnBoardingService
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.Stetho
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class Misobo : Application() {

    companion object {
        lateinit var instance: Misobo
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(this)
        Stetho.initializeWithDefaults(this)


        try {
            val registrationModel =
                RegistrationModel(
                    Settings.Secure.getString(
                        this?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
            if (SharedPreferenceManager.getUser() == null) {
                compositeDisposable.add(OnBoardingService.Creator.service.registerUser(
                    registrationModel
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { user ->
                        SharedPreferenceManager.setUser(user)
                        updatekarmaCoins()
                    })
            }else{
                updatekarmaCoins()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun updatekarmaCoins() {
        compositeDisposable.add(ProfileService.Creator.service.getProfile(
            userId = SharedPreferenceManager.getUser()?.data?.userId ?: 0
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ SharedPreferenceManager.setUserProfile(it)},{}))
    }

    override fun onTerminate() {
        super.onTerminate()
        compositeDisposable.dispose()
    }
}