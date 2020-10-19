package com.example.misobo.onBoarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class OnBoardingViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val categoriesLiveData: MutableLiveData<CategoriesAction> = MutableLiveData()
    val categorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val subCategorySelectedPosition: MutableLiveData<Int> = MutableLiveData(-1)
    val categories: MutableLiveData<CategoriesModel> = MutableLiveData()

    fun getOnBoardingCategories() {
        compositeDisposable.add(
            OnBoardingService.Creator.service
                .getCategories(Util.token)
                .subscribeOn(Schedulers.io())
                .map {
                    categories.postValue(it)
                    CategoriesAction.Success(it) as CategoriesAction
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
        return categories.value?.data?.get(categorySelectedPosition.value?:-1)?.subCategory
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