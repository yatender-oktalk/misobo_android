package com.example.misobo.mind.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.mind.api.MindService
import com.example.misobo.mind.models.MusicResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MindViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var mindService = MindService.Creator.service
    val musicLiveData: MutableLiveData<MusicFetchState> = MutableLiveData()

    fun fetchAllMusic() {
        compositeDisposable.add(mindService.fetchAllMusic(1)
            .subscribeOn(Schedulers.io())
            .map {
                MusicFetchState.Success(
                    it
                ) as MusicFetchState
            }
            .startWith(MusicFetchState.Loading)
            .onErrorReturn {
                MusicFetchState.Error
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                musicLiveData.postValue(it)
            })
    }
}

sealed class MusicFetchState {
    data class Success(val musicEntries: MusicResponseModel) : MusicFetchState()
    object Loading : MusicFetchState()
    object Error : MusicFetchState()
}