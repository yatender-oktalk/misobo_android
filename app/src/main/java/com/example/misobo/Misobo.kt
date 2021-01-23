package com.example.misobo

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.misobo.myProfile.ProfileService
import com.example.misobo.onBoarding.view.OnBoardingActivity
import com.example.misobo.utils.AuthState
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class Misobo : Application() {
    private val compositeDisposable = CompositeDisposable()

    companion object {
        lateinit var instance: Misobo
        var authRelay: BehaviorSubject<AuthState> = BehaviorSubject.create()


    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(instance)
        Stetho.initializeWithDefaults(this)
        initAuthRelay()
    }

    fun updatekarmaCoins() {
        compositeDisposable.add(
            ProfileService.Creator.service.getProfile(
                userId = SharedPreferenceManager.getUser()?.data?.userId ?: 0
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ SharedPreferenceManager.setUserProfile(it) }, {})
        )
    }

    fun sendFcmToken() {
        if (SharedPreferenceManager.getUserProfile() != null && SharedPreferenceManager.getUserProfile()?.data?.fcmToken == null) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    Log.d("TAG", token.toString())
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("fcm_registration_token", token.toString())
                    val userId = SharedPreferenceManager.getUser()?.data?.userId ?: 0
                    compositeDisposable.add(ProfileService.Creator.service.updatePack(
                        userId = userId,
                        jsonObject = jsonObject
                    ).switchMap { ProfileService.Creator.service.getProfile(userId) }
                        .map { profileResponse ->
                            SharedPreferenceManager.setUserProfile(
                                profileResponse
                            )
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({},{}))
                })
        }
    }


    private fun initAuthRelay() {
        compositeDisposable.add(authRelay
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                SharedPreferenceManager.setUser(null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val intent = Intent(this, OnBoardingActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, {})
            })
    }

    override fun onTerminate() {
        super.onTerminate()
        compositeDisposable.dispose()
    }
}