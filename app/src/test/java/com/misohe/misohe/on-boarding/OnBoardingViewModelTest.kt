package com.misohe.misohe.`on-boarding`

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.misohe.misohe.mock
import com.misohe.misohe.onBoarding.api.OnBoardingService
import com.misohe.misohe.onBoarding.models.CategoriesModel
import com.misohe.misohe.onBoarding.models.CategoriesRequestModel
import com.misohe.misohe.onBoarding.models.RegistrationModel
import com.misohe.misohe.onBoarding.models.User
import com.misohe.misohe.onBoarding.viewModels.CategoriesAction
import com.misohe.misohe.onBoarding.viewModels.OnBoardingViewModel
import com.misohe.misohe.onBoarding.viewModels.ResponseAction
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OnBoardingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()   //Make the AAC to run synchronously

    private lateinit var onBoardingViewModel: OnBoardingViewModel
    private val userObserver: Observer<User> = mock()
    private val categoriesObserver: Observer<CategoriesAction> = mock()
    private val categoryResponseAction: Observer<ResponseAction> = mock()
    private val subCategoryResponseAction: Observer<ResponseAction> = mock()
    private val onBoardingService: OnBoardingService = mock()

    @Before
    fun setUp() {
        onBoardingViewModel =
            OnBoardingViewModel()
        onBoardingViewModel.onBoardingService = onBoardingService
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `send device id should return user`() {
        //Arrange
        onBoardingViewModel.userLiveData.observeForever(userObserver)
        val registrationModel =
            RegistrationModel(deviceId = "1234abcd")
        val userResponse = User(
            data = User.Data(
                registrationId = 7,
                token = "ABCDtesting1234"
            )
        )
        Mockito.`when`(onBoardingService.registerUser(registrationModel))
            .thenReturn(Observable.just(userResponse))

        //Act
        onBoardingViewModel.registerUser(registrationModel)

        //Assert
        val captor = ArgumentCaptor.forClass(User::class.java)
        captor.run {
            verify(userObserver, times(1)).onChanged(capture())
            assertEquals(userResponse, value)
        }
    }

    @Test
    fun `get onboarding categories when token is valid and triggers loading and Success state`() {
        //Arrange
        onBoardingViewModel.categoriesLiveData.observeForever(categoriesObserver)
        val token = "ABCD1234testing"
        val categoryList = CategoriesModel.Category(
            1, "Stress", "",
            listOf(CategoriesModel.SubCategory(1, "sub")), false
        )
        val categoriesModel =
            CategoriesModel(
                listOf(categoryList)
            )
        Mockito.`when`(onBoardingService.getCategories())
            .thenReturn(Observable.just(categoriesModel))

        //Act
        onBoardingViewModel.getOnBoardingCategories()

        //Assert
        verify(categoriesObserver, times(1)).onChanged(CategoriesAction.Loading)
        verify(categoriesObserver, times(1)).onChanged(CategoriesAction.Success(categoriesModel))
    }

    @Test
    fun `error state is received when Data error`() {
        //Arrange
        val errorString = "Random Exception"
        onBoardingViewModel.categoriesLiveData.observeForever(categoriesObserver)
        val token = "ABCD1234testing"
        Mockito.`when`(onBoardingService.getCategories())
            .thenReturn(Observable.error(Throwable(errorString)))

        //Act
        onBoardingViewModel.getOnBoardingCategories()

        //Assert
        verify(categoriesObserver, times(1)).onChanged(CategoriesAction.Loading)
        verify(categoriesObserver, times(1)).onChanged(CategoriesAction.Failure(errorString))
    }

    @Test
    fun `save Categories success with first loading and then Success state`() {
        //Arrange
        onBoardingViewModel.categoryResponseAction.observeForever(categoryResponseAction)
        val token = "ABCD1234testing"
        Mockito.`when`(
            onBoardingService.saveCategories(
                CategoriesRequestModel(
                    listOf(1)
                ),
                1
            )
        )
            .thenReturn(Observable.just(Unit))

        //Act
        onBoardingViewModel.saveCategories(
            CategoriesRequestModel(listOf(1)), 1
        )

        //Assert
        verify(categoryResponseAction, times(1)).onChanged(ResponseAction.Loading)
        verify(categoryResponseAction, times(1)).onChanged(ResponseAction.Success)
    }

    @Test
    fun `save Categories fail with first loading and then Fail state`() {
        //Arrange
        onBoardingViewModel.categoryResponseAction.observeForever(categoryResponseAction)
        val token = "ABCD1234testing"
        Mockito.`when`(
            onBoardingService.saveCategories(
                CategoriesRequestModel(
                    listOf(1)
                ),
                1
            )
        )
            .thenReturn(Observable.error(Throwable("Random Exception")))

        //Act
        onBoardingViewModel.saveCategories(
            CategoriesRequestModel(listOf(1)), 1
        )

        //Assert
        verify(categoryResponseAction, times(1)).onChanged(ResponseAction.Loading)
        verify(categoryResponseAction, times(1)).onChanged(ResponseAction.Failure)
    }

    @Test
    fun `save subCategories success with first loading and then success State`() {
        //Arrange
        onBoardingViewModel.subCategoryResponseAction.observeForever(subCategoryResponseAction)
        val token = "ABCD1234testing"
        Mockito.`when`(
            onBoardingService.saveSubCategories(
                CategoriesRequestModel(
                    listOf(1)
                ),
                1
            )
        )
            .thenReturn(Observable.just(Unit))

        //Act
        onBoardingViewModel.saveSubCategories(
            CategoriesRequestModel(listOf(1)), 1
        )

        //Assert
        verify(subCategoryResponseAction, times(1)).onChanged(ResponseAction.Loading)
        verify(subCategoryResponseAction, times(1)).onChanged(ResponseAction.Success)
    }

    @Test
    fun `save subCategories fail with first loading and then Error State`() {
        //Arrange
        onBoardingViewModel.subCategoryResponseAction.observeForever(subCategoryResponseAction)
        val token = "ABCD1234testing"
        Mockito.`when`(
            onBoardingService.saveSubCategories(
                CategoriesRequestModel(
                    listOf(1)
                ),
                1
            )
        )
            .thenReturn(Observable.error(Throwable("Random Exception")))

        //Act
        onBoardingViewModel.saveSubCategories(
            CategoriesRequestModel(listOf(1)), 1
        )

        //Assert
        verify(subCategoryResponseAction, times(1)).onChanged(ResponseAction.Loading)
        verify(subCategoryResponseAction, times(1)).onChanged(ResponseAction.Failure)
    }
}