package com.misohe.misohe.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misohe.misohe.talkToExperts.api.ExpertsService
import com.misohe.misohe.talkToExperts.models.OtpPayload
import com.misohe.misohe.talkToExperts.viewModels.MobileRegistration
import com.misohe.misohe.utils.ErrorHandler
import com.misohe.misohe.utils.LiveSharedPreference
import com.misohe.misohe.utils.SharedPreferenceManager
import com.misohe.misohe.utils.SingleLiveEvent
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel() : ViewModel() {

    val mobileNumber: MutableLiveData<String> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    var profileService = ProfileService.Creator.service
    val profileLiveData: MutableLiveData<ProfileResponseAction> = MutableLiveData()
    val nameLiveData: MutableLiveData<FetchState> = MutableLiveData()
    val updatePhoneLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    val resendOtp: SingleLiveEvent<Unit> = SingleLiveEvent()
    val otpVerification: SingleLiveEvent<MobileRegistration> = SingleLiveEvent()
    val updateProfile: SingleLiveEvent<Unit> = SingleLiveEvent()
    val nameToast: SingleLiveEvent<Unit> = SingleLiveEvent()

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
                .subscribe {
                    nameToast.postValue(Unit)
                    updateProfile.postValue(Unit)
                }
        )
    }

    fun updatePhone(userId: Int, jsonObject: JsonObject) {
        compositeDisposable.add(
            ProfileService.Creator.service.updatePhone(userId, jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { updatePhoneLiveData.postValue(Unit) }
        )
    }

    fun resendOtp(userId: Int, jsonObject: JsonObject) {
        compositeDisposable.add(
            ProfileService.Creator.service.updatePhone(userId, jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resendOtp.postValue(Unit) }
        )
    }

    fun verifyOtp(id: Int, otpModel: OtpPayload) {
        compositeDisposable.add(ExpertsService.Creator.service.sendOtp(id, otpModel)
            .subscribeOn(Schedulers.io())
            .map {
                MobileRegistration.Success(
                    it
                ) as MobileRegistration
            }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { otpVerification.postValue(it) })
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
