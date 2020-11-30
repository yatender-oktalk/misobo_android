package com.example.misobo.myProfile

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var profileService = ProfileService.Creator.service

    fun getProfile(userId: Int) {
        compositeDisposable.add(profileService.getProfile(userId = userId)
            .subscribeOn(Schedulers.io())
            .subscribe {})
    }
}

sealed class ProfileResponseAction {
    object Success : ProfileResponseAction()
    object Loading : ProfileResponseAction()
    object Error : ProfileResponseAction()
}