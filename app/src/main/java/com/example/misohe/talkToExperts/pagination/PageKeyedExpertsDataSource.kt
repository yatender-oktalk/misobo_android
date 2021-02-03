package com.example.misohe.talkToExperts.pagination

import androidx.paging.PageKeyedDataSource
import com.example.misohe.talkToExperts.api.ExpertsService
import com.example.misohe.talkToExperts.models.ExpertCategoriesModel
import com.example.misohe.talkToExperts.models.ExpertModel
import io.reactivex.disposables.CompositeDisposable

class PageKeyedExpertsDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ExpertsService,
    private val categoryModel: ExpertCategoriesModel
) : PageKeyedDataSource<Int, ExpertModel.Expert>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ExpertModel.Expert>
    ) {

        if (categoryModel.id == -1) {
            compositeDisposable.add(
                networkService.getAllExperts(pageNo = 1)
                    .subscribe({ expertsList ->
                        callback.onResult(
                            expertsList.entries?.toMutableList()!!,
                            null,
                            if (expertsList.pageNumber!! < expertsList.totalPages!!) 2 else null
                        )
                    }, {})
            )
        } else {
            compositeDisposable.add(
                networkService.getCategoryExpertsList(categoryModel.id ?: 0, 1)
                    .subscribe({ expertsList ->
                        callback.onResult(
                            expertsList.entries?.toMutableList()!!,
                            null,
                            if (expertsList.pageNumber!! < expertsList.totalPages!!) 2 else null
                        )
                    }, {})
            )
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ExpertModel.Expert>
    ) {
        if (categoryModel.id == -1) {
            compositeDisposable.add(networkService.getAllExperts(pageNo = params.key)
                .subscribe { expertsList ->
                    callback.onResult(
                        expertsList.entries?.toMutableList()!!,
                        if (expertsList.pageNumber!! < expertsList.totalPages!!) params.key + 1 else null
                    )
                })
        } else {
            compositeDisposable.add(networkService.getCategoryExpertsList(
                categoryModel.id ?: 0,
                params.key
            )
                .subscribe { expertsList ->
                    callback.onResult(
                        expertsList.entries?.toMutableList()!!,
                        if (expertsList.pageNumber!! < expertsList.totalPages!!) params.key + 1 else null
                    )
                })
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ExpertModel.Expert>
    ) {
    }
}