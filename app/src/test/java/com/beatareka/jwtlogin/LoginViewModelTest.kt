package com.beatareka.jwtlogin

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.beatareka.jwtlogin.repository.UserRepository
import com.beatareka.jwtlogin.viewmodel.LoginViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    private val api: UserRepository = Mockito.mock(UserRepository::class.java)
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testCanSend_EmptyFields() {
        loginViewModel = LoginViewModel(api)
        loginViewModel.checkCanSend(Mockito.mock(Editable::class.java))
        assert(loginViewModel.canSend.value == false)
    }

    @Test
    fun testCanSend_FieldsWithValue() {
        loginViewModel = LoginViewModel(api)
        loginViewModel.username.value = "text"
        loginViewModel.password.value = "text"
        loginViewModel.checkCanSend(Mockito.mock(Editable::class.java))
        assert(loginViewModel.canSend.value == true)
    }

}