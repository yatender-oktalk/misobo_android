package com.example.misobo.onBoarding.api

import com.example.misobo.BuildConfig
import com.example.misobo.onBoarding.models.*
import com.example.misobo.utils.SharedPreferenceManager
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

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user/{userId}/send_sms")
    fun resendOTP(
        @Path("userId") userId: String,
        @Body phoneNumber: ResendOTPModel
    ): Observable<ResendOtpRespnse>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: OnBoardingService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.MISOBO_BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .addInterceptor {
                                it.proceed(
                                    it.request().newBuilder()
                                        .addHeader(
                                            "token",
                                            SharedPreferenceManager.getUser()?.data?.token ?: ""
                                        )
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