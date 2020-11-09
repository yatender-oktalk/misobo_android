package com.example.misobo.talkToExperts

import com.example.misobo.onBoarding.api.OnBoardingService
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.models.User
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ExpertsService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/expert_categories")
    fun getExpertsCategories(): Observable<List<ExpertCategoriesModel>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/category_experts/{category_id}")
    fun getExperts(@Path(value = "category_id") catgId:Int?): Observable<ExpertModel>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: ExpertsService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .addInterceptor {
                                it.proceed(
                                    it.request().newBuilder()
                                        .addHeader("token", token)
                                        .build()
                                )
                            }
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(ExpertsService::class.java)
            }
    }
}