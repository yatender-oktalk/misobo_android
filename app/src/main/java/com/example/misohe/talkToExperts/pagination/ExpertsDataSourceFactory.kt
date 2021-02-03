package com.example.misohe.talkToExperts.pagination

import androidx.paging.DataSource
import com.example.misohe.talkToExperts.api.ExpertsService
import com.example.misohe.talkToExperts.models.ExpertCategoriesModel
import com.example.misohe.talkToExperts.models.ExpertModel
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