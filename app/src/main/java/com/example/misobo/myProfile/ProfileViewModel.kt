package com.example.misobo.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.utils.ErrorHandler
import com.example.misobo.utils.LiveSharedPreference
import com.example.misobo.utils.SharedPreferenceManager
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var profileService = ProfileService.Creator.service
    val profileLiveData: MutableLiveData<ProfileResponseAction> = MutableLiveData()
    val nameLiveData: MutableLiveData<FetchState> = MutableLiveData()

    private val liveSharedPreference =
        LiveSharedPreference(SharedPreferenceManager.sharedPreferences!!)

    fun getProfileLiveData() = liveSharedPreference

    fun getProfile(userId: Int) {
        compositeDisposable.add(profileService.getProfile(userId = userId)
            .subscribeOn(Schedulers.io())
            .map {
                SharedPreferenceManager.setUserProfile(it)
                ProfileResponseAction.Success(
                    it
                ) as ProfileResponseAction
            }
            .startWith(ProfileResponseAction.Loading)
            .onErrorReturn {
                ProfileResponseAction.Error(ErrorHandler.handleError(it))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { profileLiveData.postValue(it) })
    }

    fun updateName(userId: Int, jsonObject: JsonObject) {
        compositeDisposable.add(
            ProfileService.Creator.service.updatePack(userId, jsonObject)
                .switchMap { ProfileService.Creator.service.getProfile(userId) }
                .map { profileResponse -> SharedPreferenceManager.setUserProfile(profileResponse) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }
}

sealed class ProfileResponseAction {
    data class Success(val response: ProfileResponseModel) : ProfileResponseAction()
    object Loading : ProfileResponseAction()
    data class Error(val error: String) : ProfileResponseAction()
}

sealed class FetchState {
    object Success : FetchState()
    object Loading : FetchState()
    data class Error(val error: String) : FetchState()
}
