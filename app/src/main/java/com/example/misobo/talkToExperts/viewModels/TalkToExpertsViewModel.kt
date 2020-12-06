package com.example.misobo.talkToExperts.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.misobo.mind.models.OrderPayload
import com.example.misobo.mind.models.OrderResponse
import com.example.misobo.mind.viewModels.OrderFetchState
import com.example.misobo.myProfile.FetchState
import com.example.misobo.talkToExperts.models.VerificationResponse
import com.example.misobo.talkToExperts.api.ExpertsService
import com.example.misobo.talkToExperts.models.*
import com.example.misobo.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TalkToExpertsViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val categoriesExpertLiveData: MutableLiveData<CategoriesState> = MutableLiveData()
    val expertListLiveData: MutableLiveData<ExpertListState> = MutableLiveData()
    val selectedExpertLiveDate: MutableLiveData<ExpertModel.Expert> = MutableLiveData()
    val slotListLiveData: MutableLiveData<SlotFetchState> = MutableLiveData()
    val slotSelectedLiveData: MutableLiveData<Long> = MutableLiveData()
    val bookSlotLiveData: SingleLiveEvent<BookSlotState> = SingleLiveEvent()
    val mobileRegistration: SingleLiveEvent<MobileRegistration> = SingleLiveEvent()
    val otpVerification: MutableLiveData<MobileRegistration> = MutableLiveData()
    val mobileNumber: MutableLiveData<String> = MutableLiveData()
    val orderLiveData: MutableLiveData<OrderFetchState> = MutableLiveData()
    val currentOrder: MutableLiveData<OrderResponse> = MutableLiveData()
    val captureOrderLiveData: MutableLiveData<CaptureOrderState> = MutableLiveData()
    val packsLiveData: MutableLiveData<PacksFetchState> = MutableLiveData()
    var expertsService = ExpertsService.Creator.service
    var paymentAmount = 1.00
    var pack = 1000



    fun getExpertCategories() {
        compositeDisposable.add(expertsService.getExpertsCategories()
            .subscribeOn(Schedulers.io())
            .map {
                CategoriesState.Success(
                    it
                ) as CategoriesState
            }
            .startWith(CategoriesState.Loading)
            .onErrorReturn {
                CategoriesState.Fail
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { categoriesExpertLiveData.postValue(it) })
    }

    fun getCategoryExpertsList(id: Int) {
        compositeDisposable.add(expertsService.getCategoryExpertsList(id)
            .subscribeOn(Schedulers.io())
            .map {
                ExpertListState.Success(
                    it
                ) as ExpertListState
            }
            .startWith(ExpertListState.Loading)
            .onErrorReturn { ExpertListState.Fail }
            .subscribe { expertListLiveData.postValue(it) })
    }

    fun getAllExpertsList() {
        compositeDisposable.add(expertsService.getAllExperts(1)
            .subscribeOn(Schedulers.io())
            .map {
                ExpertListState.Success(
                    it
                ) as ExpertListState
            }
            .startWith(ExpertListState.Loading)
            .onErrorReturn { ExpertListState.Fail }
            .subscribe { expertListLiveData.postValue(it) })
    }

    fun getSlot(expertId: Int, datePayloadModel: DatePayloadModel) {
        compositeDisposable.add(expertsService.getExpertsSlot(expertId, datePayloadModel)
            .subscribeOn(Schedulers.io())
            .map {
                SlotFetchState.Success(
                    it
                ) as SlotFetchState
            }
            .startWith(SlotFetchState.Loading)
            .onErrorReturn { SlotFetchState.Fail }
            .subscribe { slotListLiveData.postValue(it) })
    }

    fun mobileRegistration(otpModel: OtpPayload) {
        compositeDisposable.add(expertsService.mobileRegistration(otpModel)
            .subscribeOn(Schedulers.io())
            .map {
                MobileRegistration.Success(
                    it
                ) as MobileRegistration
            }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { mobileRegistration.postValue(it) })
    }

    fun verifyOtp(id: Int, otpModel: OtpPayload) {
        compositeDisposable.add(expertsService.sendOtp(id, otpModel)
            .subscribeOn(Schedulers.io())
            .map {
                MobileRegistration.Success(
                    it
                ) as MobileRegistration
            }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { otpVerification.postValue(it) })
    }

    fun bookSlot(expertId: Int, payload: BookSlotPayload) {
        compositeDisposable.add(expertsService.bookSlot(expertId, payload)
            .subscribeOn(Schedulers.io())
            .map {
                BookSlotState.Success(
                    it
                ) as BookSlotState
            }
            .startWith(BookSlotState.Loading)
            .onErrorReturn { e ->
                val exception = e as com.jakewharton.retrofit2.adapter.rxjava2.HttpException
                if (exception.code() == 401)
                    BookSlotState.NotAuthorised
                else if (exception.code() == 402)
                    BookSlotState.NotSufficientKarma
                else
                    BookSlotState.Fail
            }
            .subscribe { bookSlotLiveData.postValue(it) })
    }

    fun createOrder(orderPayload: OrderPayload) {
        compositeDisposable.add(expertsService.createOrder(
            orderPayload = orderPayload
        )
            .subscribeOn(Schedulers.io())
            .map {
                OrderFetchState.Success(it)
                        as OrderFetchState
            }
            .startWith(OrderFetchState.Loading)
            .onErrorReturn {
                OrderFetchState.Error(it.localizedMessage)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                orderLiveData.postValue(it)
            })
    }

    fun captureOrder(captureOrderPayload: CaptureOrderPayload) {
        compositeDisposable.add(expertsService.captureOrder(
            captureOrderPayload = captureOrderPayload
        )
            .subscribeOn(Schedulers.io())
            .map {
                CaptureOrderState.Success(it)
                        as CaptureOrderState
            }
            .startWith(CaptureOrderState.Loading)
            .onErrorReturn {
                CaptureOrderState.Fail
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                captureOrderLiveData.postValue(it)
            })
    }

    fun getPacks() {
        compositeDisposable.add(expertsService.getPacks()
            .subscribeOn(Schedulers.io())
            .map {
                PacksFetchState.Success(it)
                        as PacksFetchState
            }
            .startWith(PacksFetchState.Loading)
            .onErrorReturn {
                PacksFetchState.Fail
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                packsLiveData.postValue(it)
            })
    }
}

sealed class CategoriesState {
    data class Success(val categoriesModel: List<ExpertCategoriesModel>) : CategoriesState()
    object Fail : CategoriesState()
    object Loading : CategoriesState()
}

sealed class ExpertListState() {
    data class Success(val expertList: ExpertModel) : ExpertListState()
    object Fail : ExpertListState()
    object Loading : ExpertListState()
}

sealed class SlotFetchState() {
    data class Success(val slotList: List<ExpertSlotsResponse>) : SlotFetchState()
    object Fail : SlotFetchState()
    object Loading : SlotFetchState()
}

sealed class BookSlotState() {
    data class Success(val bookSlotResponse: BookSlotResponse) : BookSlotState()
    object Fail : BookSlotState()
    object Loading : BookSlotState()
    object NotAuthorised : BookSlotState()
    object NotSufficientKarma : BookSlotState()
}

sealed class MobileRegistration() {
    data class Success(val verificationResponse: VerificationResponse) : MobileRegistration()
    object Fail : MobileRegistration()
    object Loading : MobileRegistration()
}

sealed class CaptureOrderState() {
    data class Success(val response: CapturePaymentResponse) : CaptureOrderState()
    object Fail : CaptureOrderState()
    object Loading : CaptureOrderState()
}

sealed class PacksFetchState() {
    data class Success(val packsList: List<Packs>) : PacksFetchState()
    object Fail : PacksFetchState()
    object Loading : PacksFetchState()
}