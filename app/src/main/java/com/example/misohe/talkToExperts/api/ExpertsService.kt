package com.example.misohe.talkToExperts.api

import com.example.misohe.BuildConfig
import com.example.misohe.mind.models.OrderPayload
import com.example.misohe.mind.models.OrderResponse
import com.example.misohe.myProfile.ProfileResponseModel
import com.example.misohe.talkToExperts.models.OtpPayload
import com.example.misohe.talkToExperts.models.VerificationResponse
import com.example.misohe.talkToExperts.models.*
import com.example.misohe.utils.SharedPreferenceManager
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
    fun getCategoryExpertsList(
        @Path(value = "category_id") catgId: Int?,
        @Query("page") pageNo: Int
    ): Observable<ExpertModel>

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
    ): Observable<ProfileResponseModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/order")
    fun createOrder(
        @Body orderPayload: OrderPayload
    ): Observable<OrderResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/order/capture")
    fun captureOrder(
        @Body captureOrderPayload: CaptureOrderPayload
    ): Observable<CapturePaymentResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/packs")
    fun getPacks(
    ): Observable<List<Packs>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/user/{userId}/expert_bookings")
    fun fetchBookings(
        @Path(value = "userId") userId: String,
        @Query(value = "page") pageNumber: Int
    ): Observable<UserBookings>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/rating")
    fun submitRating(
        @Body payload: RatingPayload
    ): Observable<RatingResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(".")
    fun slotList(@Query(value = "param") payload: String): Observable<Unit>

    object Creator {
        val service: ExpertsService
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
                return retrofit.create(ExpertsService::class.java)
            }

        val encryptService: ExpertsService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.MANGALPOOJAN_BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(ExpertsService::class.java)
            }
    }
}