package com.example.misohe.rewards

import com.example.misohe.BuildConfig
import com.example.misohe.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RewardsService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/rewards")
    fun getRewards(): Observable<RewardsModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/rewards/{id}/redeem")
    fun redeemReward(@Path("id") rewardId: Int?): Observable<RewardsModel.Reward>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/rewards/{userId}/redeemed")
    fun getClaimedRewards(@Path("userId") userId: Int?): Observable<ClaimedRewardsModel>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: RewardsService
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
                return retrofit.create(RewardsService::class.java)
            }
    }
}