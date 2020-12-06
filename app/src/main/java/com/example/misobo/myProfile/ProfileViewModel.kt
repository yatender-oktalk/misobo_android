package com.example.misobo.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.utils.SharedPreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var profileService = ProfileService.Creator.service
    val profileLiveData: MutableLiveData<ProfileResponseAction> = MutableLiveData()
    val nameLiveData: MutableLiveData<FetchState> = MutableLiveData()

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
                ProfileResponseAction.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { profileLiveData.postValue(it) })
    }

    fun updateName(userId: Int, namePayload: NamePayload) {
        compositeDisposable.add(profileService.updateName(
            userId = userId,
            namePayload = namePayload)
            .subscribeOn(Schedulers.io())
            .map {
                FetchState.Success as FetchState
            }
            .startWith(FetchState.Loading)
            .onErrorReturn {
                FetchState.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { nameLiveData.postValue(it) })
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