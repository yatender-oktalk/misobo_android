package com.example.misobo.talkToExperts.api

import com.example.misobo.mind.models.OrderPayload
import com.example.misobo.mind.models.OrderResponse
import com.example.misobo.talkToExperts.models.OtpPayload
import com.example.misobo.talkToExperts.models.VerificationResponse
import com.example.misobo.talkToExperts.models.*
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
    fun getCategoryExpertsList(@Path(value = "category_id") catgId: Int?): Observable<ExpertModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/experts")
    fun getAllExperts(@Query("page") pageNo: Int): Observable<ExpertModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/expert/{expert_id}/slots")
    fun getExpertsSlot(
        @Path("expert_id") expertId: Int,
        @Body dateModel: DatePayloadModel
    ): Observable<List<ExpertSlotsResponse>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/expert/{expert_id}/book_slot")
    fun bookSlot(
        @Path("expert_id") expertId: Int,
        @Body payload: BookSlotPayload
    ): Observable<BookSlotResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user")
    fun mobileRegistration(
        @Body payload: OtpPayload
    ): Observable<VerificationResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user/{id}/verify")
    fun sendOtp(
        @Path("id") id: Int,
        @Body payload: OtpPayload
    ): Observable<VerificationResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/order")
    fun createOrder(
        @Body orderPayload: OrderPayload
    ): Observable<OrderResponse>

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
                                        .addHeader("token",
                                            token
                                        )
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