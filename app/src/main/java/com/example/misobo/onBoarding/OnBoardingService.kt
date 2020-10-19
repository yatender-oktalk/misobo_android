package com.example.misobo.onBoarding

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface OnBoardingService {

    @GET("api/categories")
    fun getCategories(
        @Header("token") token: String
    ):Observable<CategoriesModel>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        val service: OnBoardingService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(OnBoardingService::class.java)
            }
    }
}