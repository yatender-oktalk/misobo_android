package com.example.misobo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OnBoardingViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val categoriesLiveData: MutableLiveData<CategoriesAction> = MutableLiveData()

    fun getOnBoardingCategories() {
        compositeDisposable.add(
            OnBoardingService.Creator.service
                .getCategories(Util.token)
                .subscribeOn(Schedulers.io())
                .map { CategoriesAction.Success(it) as CategoriesAction }
                .startWith (CategoriesAction.Loading)
                .onErrorReturn { CategoriesAction.Failure(it.localizedMessage) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categoriesLiveData.postValue(it) }
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