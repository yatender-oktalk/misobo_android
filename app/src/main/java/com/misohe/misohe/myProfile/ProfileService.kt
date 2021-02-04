package com.misohe.misohe.myProfile

import com.misohe.misohe.BuildConfig
import com.misohe.misohe.mind.models.SuccessResponse
import com.misohe.misohe.onBoarding.models.ResendOtpRespnse
import com.misohe.misohe.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ProfileService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/user/{userId}")
    fun getProfile(
        @Path(value = "userId") userId: Int
    ): Observable<ProfileResponseModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("api/user/{userId}")
    fun updateName(
        @Path(value = "userId") userId: Int,
        @Body namePayload: NamePayload
    ): Observable<SuccessResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PUT("api/user/{userId}")
    fun updatePack(
        @Path(value = "userId") userId: Int,
        @Body jsonObject: JsonObject
    ): Observable<SuccessResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user/{userId}/send_sms")
    fun updatePhone(
        @Path(value = "userId") userId: Int,
        @Body jsonObject: JsonObject
    ): Observable<ResendOtpRespnse>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: ProfileService
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
                return retrofit.create(ProfileService::class.java)
            }
    }
}