package com.misohe.misohe.bmi.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misohe.misohe.bmi.models.BmiRequestBody
import com.misohe.misohe.bmi.models.BmiResponsebody
import com.misohe.misohe.bmi.api.BmiService
import com.misohe.misohe.myProfile.ProfileService
import com.misohe.misohe.utils.SharedPreferenceManager
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BmiViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var bmiService = BmiService.Creator.service
    val height:MutableLiveData<Double> = MutableLiveData()
    val weight:MutableLiveData<Int> = MutableLiveData()
    val gender:MutableLiveData<String> = MutableLiveData()

    val bmiDetails:MutableLiveData<BmiResponsebody> = MutableLiveData()
    val bmiActionLiveData: MutableLiveData<BmiResponseAction> = MutableLiveData()

    fun saveBmi(bmiRequestBody: BmiRequestBody, regId: Int) {
        compositeDisposable.add(bmiService.saveBmi(
            bmiRequestBody = bmiRequestBody,
            registrationId = regId
        )
            .subscribeOn(Schedulers.io())
            .map {
                BmiResponseAction.Success(
                    it
                ) as BmiResponseAction
            }
            .startWith(BmiResponseAction.Loading)
            .onErrorReturn {
                BmiResponseAction.Error
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                bmiActionLiveData.postValue(it)
            })
    }

    fun updatePackUnlock(userId: Int, jsonObject: JsonObject) {
        compositeDisposable.add(
            ProfileService.Creator.service.updatePack(userId, jsonObject)
                .switchMap { ProfileService.Creator.service.getProfile(userId) }
                .map { profileResponse -> SharedPreferenceManager.setUserProfile(profileResponse) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    sealed class BmiResponseAction {
        data class Success(val bmiResponsebody: BmiResponsebody) : BmiResponseAction()
        object Loading : BmiResponseAction()
        object Error : BmiResponseAction()
    }
}