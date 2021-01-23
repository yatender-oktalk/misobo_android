package com.example.misobo.bmi.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.bmi.models.BmiRequestBody
import com.example.misobo.bmi.models.BmiResponsebody
import com.example.misobo.bmi.api.BmiService
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

    sealed class BmiResponseAction {
        data class Success(val bmiResponsebody: BmiResponsebody) : BmiResponseAction()
        object Loading : BmiResponseAction()
        object Error : BmiResponseAction()
    }
}