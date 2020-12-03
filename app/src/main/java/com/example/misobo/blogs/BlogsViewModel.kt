package com.example.misobo.blogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.mind.models.MusicResponseModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BlogsViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    private var blogService = BlogsService.Creator.service
    val blogLiveData: MutableLiveData<BlogsFetchState> = MutableLiveData()

    fun fetchBlogs() {
        compositeDisposable.add(blogService.fetchAllBlogs()
            .subscribeOn(Schedulers.io())
            .map { BlogsFetchState.Success(it) as BlogsFetchState }
            .startWith(BlogsFetchState.Loading)
            .onErrorReturn { BlogsFetchState.Error(it.message.toString()) }
            .subscribe { blogLiveData.postValue(it) })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class BlogsFetchState {
    data class Success(val blogsModel: BlogsModel) : BlogsFetchState()
    object Loading : BlogsFetchState()
    data class Error(val message: String) : BlogsFetchState()
}

