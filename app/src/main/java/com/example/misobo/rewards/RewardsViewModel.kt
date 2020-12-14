package com.example.misobo.rewards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RewardsViewModel : ViewModel() {

    val rewardsLiveData: MutableLiveData<RewardsFetchState> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun getRewards() {
        compositeDisposable.add(RewardsService.Creator.service.getRewards()
            .subscribeOn(Schedulers.io())
            .map { RewardsFetchState.Success(it) as RewardsFetchState }
            .startWith(RewardsFetchState.Loading)
            .onErrorReturn { RewardsFetchState.Error(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { rewardsLiveData.postValue(it) })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class RewardsFetchState() {
    data class Success(val rewardsModel: RewardsModel) : RewardsFetchState()
    object Loading : RewardsFetchState()
    data class Error(val throwable: Throwable) : RewardsFetchState()
}