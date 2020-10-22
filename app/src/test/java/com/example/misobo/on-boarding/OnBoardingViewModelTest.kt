package com.example.misobo.`on-boarding`

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.misobo.mock
import com.example.misobo.onBoarding.OnBoardingService
import com.example.misobo.onBoarding.OnBoardingViewModel
import com.example.misobo.onBoarding.RegistrationModel
import com.example.misobo.onBoarding.User
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
    private val onBoardingService: OnBoardingService = mock()

    @Before
    fun setUp() {
        onBoardingViewModel = OnBoardingViewModel()
        onBoardingViewModel.userLiveData.observeForever(userObserver)
        onBoardingViewModel.onBoardingService = onBoardingService
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `send device id should return user`() {
        //Arrange
        val registrationModel = RegistrationModel(deviceId = "1234abcd")
        val userResponse = User(data = User.Data(id = 7, token = "ABCDtesting1234"))
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
}