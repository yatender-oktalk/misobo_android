package com.example.misohe.blogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misohe.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BlogsViewModel : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    private var blogService = BlogsService.Creator.service
    val blogLiveData: MutableLiveData<BlogsFetchState> = MutableLiveData()
    val detailedBlogLiveData: SingleLiveEvent<BlogDetailedFetchState> = SingleLiveEvent()

    fun fetchBlogs() {
        compositeDisposable.add(blogService.fetchAllBlogs()
            .subscribeOn(Schedulers.io())
            .map { BlogsFetchState.Success(it) as BlogsFetchState }
            .startWith(BlogsFetchState.Loading)
            .onErrorReturn { BlogsFetchState.Error(it.message.toString()) }
            .subscribe { blogLiveData.postValue(it) })
    }

    fun getDetailedBlog(blogId: Int) {
        compositeDisposable.add(blogService.getDetailBlog(blogId)
            .subscribeOn(Schedulers.io())
            .map { BlogDetailedFetchState.Success(it) as BlogDetailedFetchState }
            .startWith(BlogDetailedFetchState.Loading)
            .onErrorReturn { BlogDetailedFetchState.Error(it.message.toString()) }
            .subscribe { detailedBlogLiveData.postValue(it) })
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

sealed class BlogDetailedFetchState {
    data class Success(val blogsModel: BlogsModel.Blogs) : BlogDetailedFetchState()
    object Loading : BlogDetailedFetchState()
    data class Error(val message: String) : BlogDetailedFetchState()
}

