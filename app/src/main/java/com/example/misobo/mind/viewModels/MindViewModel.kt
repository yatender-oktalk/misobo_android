package com.example.misobo.mind.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.mind.api.MindService
import com.example.misobo.mind.models.MusicResponseModel
import com.example.misobo.mind.models.OrderPayload
import com.example.misobo.mind.models.OrderResponse
import com.example.misobo.mind.models.ProgressPayload
import com.example.misobo.myProfile.FetchState
import com.example.misobo.myProfile.ProfileService
import com.example.misobo.utils.LiveSharedPreference
import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.utils.SingleLiveEvent
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MindViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var mindService = MindService.Creator.service
    val musicLiveData: SingleLiveEvent<MusicFetchState> = SingleLiveEvent()
    val congratsListenerLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    val progressLiveData: MutableLiveData<FetchState> = MutableLiveData()
    val playMusicLiveData: MutableLiveData<MusicResponseModel.MusicModel> = MutableLiveData()
    var mutableMusicList: MutableList<MusicResponseModel.MusicModel> = mutableListOf()
    val musicList: MutableLiveData<List<MusicResponseModel.MusicModel>> = MutableLiveData()
    var selectedMusicId: Int = 0
    private val liveSharedPreference =
        LiveSharedPreference(SharedPreferenceManager.sharedPreferences!!)

    fun getCoinsLiveData() = liveSharedPreference

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

    fun updateProgress(musicId: Int, progressPayload: ProgressPayload) {
        compositeDisposable.add(mindService.updateProgress(
            musicId,
            progressPayload = progressPayload
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                /*musicList.value?.find { it.id == (response.data.id) }?.progress =
                    response.data.progress?.toInt()*/
                musicList.postValue(mutableMusicList.apply {
                    find { it.id == (response.data.id) }?.progress =
                        response.data.progress?.toInt()
                })
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

sealed class OrderFetchState {
    data class Success(val orderResponse: OrderResponse) : OrderFetchState()
    object Loading : OrderFetchState()
    data class Error(val message: String) : OrderFetchState()
}