package com.misohe.misohe.mind.pagination

import androidx.paging.DataSource
import com.misohe.misohe.mind.api.MindService
import com.misohe.misohe.mind.models.MusicResponseModel
import io.reactivex.disposables.CompositeDisposable

class MusicDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: MindService,
    private val userId: String
) : DataSource.Factory<Int, MusicResponseModel.MusicModel>() {

    override fun create(): DataSource<Int, MusicResponseModel.MusicModel> {
        return PageKeyedMusicDataSource(
            compositeDisposable = compositeDisposable,
            networkService = networkService,
            userId = userId
        )
    }
}