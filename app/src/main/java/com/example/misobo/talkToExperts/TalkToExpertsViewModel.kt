package com.example.misobo.talkToExperts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val bookSlotLiveData: MutableLiveData<BookSlotState> = MutableLiveData()
    val mobileRegistration: MutableLiveData<MobileRegistration> = MutableLiveData()
    val otpVerification: MutableLiveData<MobileRegistration> = MutableLiveData()
    val mobileNumber:MutableLiveData<String> = MutableLiveData()

    var talkToExpertsViewModel = ExpertsService.Creator.service

    fun getExpertCategories() {
        compositeDisposable.add(talkToExpertsViewModel.getExpertsCategories()
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
        compositeDisposable.add(talkToExpertsViewModel.getCategoryExpertsList(id)
            .subscribeOn(Schedulers.io())
            .map { ExpertListState.Success(it) as ExpertListState }
            .startWith(ExpertListState.Loading)
            .onErrorReturn { ExpertListState.Fail }
            .subscribe { expertListLiveData.postValue(it) })
    }

    fun getAllExpertsList() {
        compositeDisposable.add(talkToExpertsViewModel.getAllExperts(1)
            .subscribeOn(Schedulers.io())
            .map { ExpertListState.Success(it) as ExpertListState }
            .startWith(ExpertListState.Loading)
            .onErrorReturn { ExpertListState.Fail }
            .subscribe { expertListLiveData.postValue(it) })
    }

    fun getSlot(expertId: Int, datePayloadModel: DatePayloadModel) {
        compositeDisposable.add(talkToExpertsViewModel.getExpertsSlot(expertId, datePayloadModel)
            .subscribeOn(Schedulers.io())
            .map { SlotFetchState.Success(it) as SlotFetchState }
            .startWith(SlotFetchState.Loading)
            .onErrorReturn { SlotFetchState.Fail }
            .subscribe { slotListLiveData.postValue(it) })
    }

    fun mobileRegistration(otpModel: OtpPayload) {
        compositeDisposable.add(talkToExpertsViewModel.mobileRegistration(otpModel)
            .subscribeOn(Schedulers.io())
            .map { MobileRegistration.Success(it) as MobileRegistration }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { mobileRegistration.postValue(it) })
    }

    fun verifyOtp(id: Int, otpModel: OtpPayload) {
        compositeDisposable.add(talkToExpertsViewModel.sendOtp(id, otpModel)
            .subscribeOn(Schedulers.io())
            .map { MobileRegistration.Success(it) as MobileRegistration }
            .startWith(MobileRegistration.Loading)
            .onErrorReturn { MobileRegistration.Fail }
            .subscribe { otpVerification.postValue(it) })
    }

    fun bookSlot(expertId: Int, payload: BookSlotPayload) {
        compositeDisposable.add(talkToExpertsViewModel.bookSlot(expertId, payload)
            .subscribeOn(Schedulers.io())
            .map { BookSlotState.Success(it) as BookSlotState }
            .startWith(BookSlotState.Loading)
            .onErrorReturn { e ->
                val exception = e as com.jakewharton.retrofit2.adapter.rxjava2.HttpException
                if (exception.code() == 401)
                    BookSlotState.NotAuthorised
                else
                    BookSlotState.Fail
            }
            .subscribe { bookSlotLiveData.postValue(it) })
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
}

sealed class MobileRegistration() {
    data class Success(val verificationResponse: VerificationResponse) : MobileRegistration()
    object Fail : MobileRegistration()
    object Loading : MobileRegistration()
}