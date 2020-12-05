package com.example.misobo.mind.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.mind.api.MindService
import com.example.misobo.mind.models.MusicResponseModel
import com.example.misobo.mind.models.ProgressPayload
import com.example.misobo.myProfile.FetchState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MindViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var mindService = MindService.Creator.service
    val musicLiveData: MutableLiveData<MusicFetchState> = MutableLiveData()
    val progressLiveData: MutableLiveData<FetchState> = MutableLiveData()
    val playMusicLiveData: MutableLiveData<MusicResponseModel.MusicModel> = MutableLiveData()
    var selectedMusicId: Int = 0


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
                MusicFetchState.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                musicLiveData.postValue(it)
            })
    }

    fun updateProgress(musicId: Int, progressPayload: ProgressPayload) {
        compositeDisposable.add(mindService.updateProgress(
            musicId,
            progressPayload = progressPayload
        )
            .subscribeOn(Schedulers.io())
            .map {
                FetchState.Success
                 as FetchState
            }
            .startWith(FetchState.Loading)
            .onErrorReturn {
                FetchState.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                progressLiveData.postValue(it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}


sealed class MusicFetchState {
    data class Success(val musicEntries: List<MusicResponseModel.MusicModel>) : MusicFetchState()
    object Loading : MusicFetchState()
    data class Error(val message: String) : MusicFetchState()
}