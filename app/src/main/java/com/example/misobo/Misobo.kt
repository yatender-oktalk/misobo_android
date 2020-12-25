package com.example.misobo

import android.app.Application
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.myProfile.ProfileResponseAction
import com.example.misobo.myProfile.ProfileService
import com.example.misobo.onBoarding.api.OnBoardingService
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.view.OnBoardingActivity
import com.example.misobo.onBoarding.viewModels.OnBoardingViewModel
import com.example.misobo.utils.AuthState
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class Misobo : Application() {

    companion object {
        lateinit var instance: Misobo
        var authRelay: BehaviorSubject<AuthState> = BehaviorSubject.create()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPreferenceManager.init(instance)
        Stetho.initializeWithDefaults(this)
        initAuthRelay()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                Log.d("TAG", token.toString())
                Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
            })


        try {
            if (SharedPreferenceManager.getUser() == null) {
            } else {
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
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