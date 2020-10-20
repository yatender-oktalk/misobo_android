package com.example.misobo.onBoarding

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
    fun getCategories(
        @Header("token") token: String
    ): Observable<CategoriesModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("/api/registration/{registration_id}/categories")
    fun saveCategories(
        @Header("token") token: String,
        @Body categoriesRequestModel: CategoriesRequestModel,
        @Path(value = "registration_id") registrationId: Int
    ): Observable<Unit>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("/api/registration/{registration_id}/sub_categories")
    fun saveSubCategories(
        @Header("token") token: String,
        @Body categoriesRequestModel: CategoriesRequestModel,
        @Path(value = "registration_id") registrationId: Int
    ): Observable<Unit>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/registration")
    fun registerUser(@Body deviceId: RegistrationModel): Observable<User>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        val service: OnBoardingService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(OnBoardingService::class.java)
            }
    }
}