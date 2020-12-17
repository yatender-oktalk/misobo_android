package com.example.misobo.rewards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.myProfile.FetchState
import com.example.misobo.talkToExperts.viewModels.BookSlotState
import com.example.misobo.utils.ErrorHandler
import com.example.misobo.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RewardsViewModel : ViewModel() {

    val rewardsLiveData: MutableLiveData<RewardsFetchState> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    val selectedRewardLiveData: MutableLiveData<RewardsModel.Reward> = MutableLiveData()
    val redeemRewardsLiveData: SingleLiveEvent<RedeemFetchState> = SingleLiveEvent()
    val claimedRewardsLiveData: MutableLiveData<ClaimedRewardsFetchState> = MutableLiveData()
    val claimedRewardsList: MutableLiveData<List<ClaimedRewardsModel.ClaimedReward>> =
        MutableLiveData()

    fun getRewards() {
        compositeDisposable.add(RewardsService.Creator.service.getRewards()
            .subscribeOn(Schedulers.io())
            .map { RewardsFetchState.Success(it) as RewardsFetchState }
            .startWith(RewardsFetchState.Loading)
            .onErrorReturn {
                RewardsFetchState.Error(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { rewardsLiveData.postValue(it) })
    }

    fun redeemRewards(id: Int) {
        compositeDisposable.add(RewardsService.Creator.service.redeemReward(id)
            .subscribeOn(Schedulers.io())
            .map { RedeemFetchState.Success as RedeemFetchState }
            .startWith(RedeemFetchState.Loading)
            .onErrorReturn {
                val exception = it as com.jakewharton.retrofit2.adapter.rxjava2.HttpException
                if (exception.code() == 402)
                    RedeemFetchState.NotSufficientKarma
                else
                    RedeemFetchState.Error
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { redeemRewardsLiveData.postValue(it) })
    }

    fun claimedRewards(userId: Int) {
        compositeDisposable.add(RewardsService.Creator.service.getClaimedRewards(userId)
            .subscribeOn(Schedulers.io())
            .map { ClaimedRewardsFetchState.Success(it) as ClaimedRewardsFetchState }
            .startWith(ClaimedRewardsFetchState.Loading)
            .onErrorReturn { ClaimedRewardsFetchState.Error(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { claimedRewardsLiveData.postValue(it) })
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

sealed class ClaimedRewardsFetchState() {
    data class Success(val claimedRewardsModel: ClaimedRewardsModel) : ClaimedRewardsFetchState()
    object Loading : ClaimedRewardsFetchState()
    data class Error(val throwable: Throwable) : ClaimedRewardsFetchState()
}

sealed class RedeemFetchState() {
    object Success : RedeemFetchState()
    object Loading : RedeemFetchState()
    object NotSufficientKarma : RedeemFetchState()
    object Error : RedeemFetchState()
}