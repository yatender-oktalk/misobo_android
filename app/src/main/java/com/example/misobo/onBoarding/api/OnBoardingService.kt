package com.example.misobo.onBoarding.api

import com.example.misobo.utils.SharedPreferenceManager
import com.example.misobo.onBoarding.models.CategoriesModel
import com.example.misobo.onBoarding.models.CategoriesRequestModel
import com.example.misobo.onBoarding.models.RegistrationModel
import com.example.misobo.onBoarding.models.User
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OnBoardingService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/categories")
    fun getCategories(): Observable<CategoriesModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("/api/registration/{registration_id}/categories")
    fun saveCategories(
        @Body categoriesRequestModel: CategoriesRequestModel,
        @Path(value = "registration_id") registrationId: Int
    ): Observable<Unit>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("/api/registration/{registration_id}/sub_categories")
    fun saveSubCategories(
        @Body categoriesRequestModel: CategoriesRequestModel,
        @Path(value = "registration_id") registrationId: Int
    ): Observable<Unit>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/registration")
    fun registerUser(@Body deviceId: RegistrationModel): Observable<User>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: OnBoardingService
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
                return retrofit.create(OnBoardingService::class.java)
            }
    }
}