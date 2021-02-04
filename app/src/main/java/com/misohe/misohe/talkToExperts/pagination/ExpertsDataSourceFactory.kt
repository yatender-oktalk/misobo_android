package com.misohe.misohe.talkToExperts.pagination

import androidx.paging.DataSource
import com.misohe.misohe.talkToExperts.api.ExpertsService
import com.misohe.misohe.talkToExperts.models.ExpertCategoriesModel
import com.misohe.misohe.talkToExperts.models.ExpertModel
import io.reactivex.disposables.CompositeDisposable

class ExpertsDataSourceFactory(
    val compositeDisposable: CompositeDisposable,
    val expertsService: ExpertsService,
    val categoryModel: ExpertCategoriesModel
) : DataSource.Factory<Int, ExpertModel.Expert>() {
    override fun create(): DataSource<Int, ExpertModel.Expert> {
        return PageKeyedExpertsDataSource(
            compositeDisposable = compositeDisposable,
            networkService = expertsService,
            categoryModel = categoryModel
        )
    }
}