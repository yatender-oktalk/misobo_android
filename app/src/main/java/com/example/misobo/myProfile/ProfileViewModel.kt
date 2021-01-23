package com.example.misobo.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var profileService = ProfileService.Creator.service
    val profileLiveData: MutableLiveData<ProfileResponseAction> = MutableLiveData()

    fun getProfile(userId: Int) {
        compositeDisposable.add(profileService.getProfile(userId = userId)
            .subscribeOn(Schedulers.io())
            .map {
                ProfileResponseAction.Success(
                    it
                ) as ProfileResponseAction
            }
            .startWith(ProfileResponseAction.Loading)
            .onErrorReturn {
                ProfileResponseAction.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { profileLiveData.postValue(it) })
    }
}

sealed class ProfileResponseAction {
    data class Success(val response: ProfileResponseModel) : ProfileResponseAction()
    object Loading : ProfileResponseAction()
    data class Error(val error: String) : ProfileResponseAction()
}