package com.misohe.misohe.onBoarding.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misohe.misohe.myProfile.ProfileService
import com.misohe.misohe.utils.SingleLiveEvent
import com.misohe.misohe.onBoarding.api.OnBoardingService
import com.misohe.misohe.onBoarding.models.*
import com.misohe.misohe.talkToExperts.api.ExpertsService
import com.misohe.misohe.talkToExperts.models.OtpPayload
import com.misohe.misohe.talkToExperts.viewModels.MobileRegistration
import com.misohe.misohe.utils.ErrorHandler
import com.misohe.misohe.utils.SharedPreferenceManager
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OnBoardingViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    //Category Selection
    val categoriesLiveData: MutableLiveData<CategoriesAction> = MutableLiveData()
    val categorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val subCategorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val categories: MutableLiveData<CategoriesModel> = MutableLiveData()
    val categoryResponseAction: SingleLiveEvent<ResponseAction> =
        SingleLiveEvent()
    val subCategoryResponseAction: SingleLiveEvent<ResponseAction> =
        SingleLiveEvent()
    var onBoardingService =
        OnBoardingService.Creator.service

    val reminderTime: MutableLiveData<ReminderTime> = MutableLiveData()
    val userLiveData: SingleLiveEvent<User> = SingleLiveEvent()
    val startMainActivityTrigger: MutableLiveData<Boolean> = MutableLiveData()

    //Registration
    val mobileRegistration: SingleLiveEvent<MobileRegistration> = SingleLiveEvent()
    val otpVerification: SingleLiveEvent<MobileRegistration> = SingleLiveEvent()
    val mobileNumber: MutableLiveData<String> = MutableLiveData()
    val resendOTPAction: MutableLiveData<ResendOTPAction> = MutableLiveData()

    fun getOnBoardingCategories() {
        compositeDisposable.add(
            OnBoardingService.Creator.service
                .getCategories()
                .subscribeOn(Schedulers.io())
                .map {
                    categories.postValue(it)
                    CategoriesAction.Success(
                        it
                    ) as CategoriesAction
                }
                .startWith(CategoriesAction.Loading)
                .onErrorReturn {
                    CategoriesAction.Failure(
                        it.localizedMessage
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    categoriesLiveData.postValue(it)
                }
        )
    }

    fun getSubCategory(): List<CategoriesModel.SubCategory>? {
        return categories.value?.data?.get(categorySelectedPosition.value ?: -1)?.subCategory
    }

    fun registerUser(registrationModel: RegistrationModel) {
        compositeDisposable.add(
            OnBoardingService.Creator.service.registerUser(registrationModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user ->
                    userLiveData.postValue(user)
                    SharedPreferenceManager.setUser(user)
                }
        )
    }

    fun sendDailyReminder(userId: Int, reminderJson: JsonObject) {
        compositeDisposable.add(ProfileService.Creator.service.updatePack(userId, reminderJson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { startMainActivityTrigger.postValue(true) })
    }

    fun resendOTP(userId: String, resendOTPModel: ResendOTPModel) {
        compositeDisposable.add(
            OnBoardingService.Creator.service.resendOTP(userId, resendOTPModel)
                .subscribeOn(Schedulers.io())
                .map { ResendOTPAction.Success(it) as ResendOTPAction }
                .startWith(ResendOTPAction.Loading)
                .onErrorReturn { ResendOTPAction.Failure(ErrorHandler.handleError(it)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    resendOTPAction.postValue(it)
                }
        )
    }


    fun saveCategories(
        categoryModel: CategoriesRequestModel,
        regId: Int
    ) {
        compositeDisposable.add(
            onBoardingService
                .saveCategories(
                    categoriesRequestModel = categoryModel,
                    registrationId = regId
                )
                .subscribeOn(Schedulers.io())
                .map { ResponseAction.Success as ResponseAction }
                .startWith(ResponseAction.Loading)
                .onErrorReturn { ResponseAction.Failure }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categoryResponseAction.postValue(it) }
        )
    }

    fun saveSubCategories(categoryModel: CategoriesRequestModel, regId: Int) {
        compositeDisposable.add(
            onBoardingService
                .saveSubCategories(
                    categoriesRequestModel = categoryModel,
                    registrationId = regId
                )
                .subscribeOn(Schedulers.io())
                .map { ResponseAction.Success as ResponseAction }
                .startWith(ResponseAction.Loading)
                .onErrorReturn { ResponseAction.Failure }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { subCategoryResponseAction.postValue(it) }
        )
    }

    /* fun mobileRegistration(otpModel: OtpPayload) {
         compositeDisposable.add(ExpertsService.Creator.service.mobileRegistration(otpModel)
             .subscribeOn(Schedulers.io())
             .map {
                 MobileRegistration.Success(
                     it
                 ) as MobileRegistration
             }
             .startWith(MobileRegistration.Loading)
             .onErrorReturn { MobileRegistration.Fail }
             .subscribe { mobileRegistration.postValue(it) })
     }
 */
    fun verifyOtp(id: Int, otpModel: OtpPayload) {
        compositeDisposable.add(ExpertsService.Creator.service.sendOtp(id, otpModel)
            .subscribeOn(Schedulers.io())
            .map {
                SharedPreferenceManager.setUserProfile(it)
                MobileRegistration.Success(
                    it
                ) as MobileRegistration
            }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { otpVerification.postValue(it) })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class CategoriesAction {
    data class Success(val categoryModel: CategoriesModel) : CategoriesAction()
    object Loading : CategoriesAction()
    data class Failure(val localizedMessage: String?) : CategoriesAction()
}

sealed class ResendOTPAction {
    data class Success(val it: ResendOtpRespnse) : ResendOTPAction()
    object Loading : ResendOTPAction()
    data class Failure(val message: String) : ResendOTPAction()
}

sealed class ReminderTime {
    data class SelectedTime(val hour: String, val minutes: String, val amPm: Int) : ReminderTime()
}

sealed class ResponseAction {
    object Success : ResponseAction()
    object Failure : ResponseAction()
    object Loading : ResponseAction()
}

