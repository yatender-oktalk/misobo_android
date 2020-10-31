package com.example.misobo.onBoarding.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.utils.SingleLiveEvent
import com.example.misobo.onBoarding.api.OnBoardingService
import com.example.misobo.onBoarding.models.CategoriesModel
import com.example.misobo.onBoarding.models.CategoriesRequestModel
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OnBoardingViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val categoriesLiveData: MutableLiveData<CategoriesAction> = MutableLiveData()
    val categorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val subCategorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val categories: MutableLiveData<CategoriesModel> = MutableLiveData()
    val reminderTime: MutableLiveData<ReminderTime> = MutableLiveData()
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val categoryResponseAction: SingleLiveEvent<ResponseAction> =
        SingleLiveEvent()
    val subCategoryResponseAction: SingleLiveEvent<ResponseAction> =
        SingleLiveEvent()
    var onBoardingService =
        OnBoardingService.Creator.service

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
            onBoardingService.registerUser(registrationModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user -> userLiveData.postValue(user) }
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

sealed class ReminderTime {
    data class SelectedTime(val hour: String, val minutes: String, val amPm: Int) : ReminderTime()
}

sealed class ResponseAction {
    object Success : ResponseAction()
    object Failure : ResponseAction()
    object Loading : ResponseAction()
}

