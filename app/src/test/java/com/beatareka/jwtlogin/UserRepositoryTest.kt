package com.beatareka.jwtlogin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.beatareka.jwtlogin.network.UserAPI
import com.beatareka.jwtlogin.network.dto.LoginResponse
import com.beatareka.jwtlogin.repository.TokenStoreRepository
import com.beatareka.jwtlogin.repository.UserRepository
import com.beatareka.jwtlogin.repository.UserRepositoryImpl
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    private val userAPI = Mockito.mock(UserAPI::class.java)
    private val repository = Mockito.mock(TokenStoreRepository::class.java)

    private lateinit var userRepository: UserRepository;


    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testLoginOkResponse() {
        userRepository = UserRepositoryImpl(userAPI, repository)
        val loginResponse = LoginResponse("", "", 1, "")
        Mockito.`when`(userAPI.login(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Single.just(Response.success(loginResponse)))
        val login = userRepository.login("text", "text").blockingGet()
        assert(login.code == 200)
        assert(login.message == "OK")
        verify(repository, times(1)).saveToken(anyString())
        verify(repository, times(1)).saveRefreshToken(anyString())
    }


    @Test
    fun testLoginUnauthorizedResponse() {
        userRepository = UserRepositoryImpl(userAPI, repository)
        Mockito.`when`(userAPI.login(any(), any(), any(), any()))
            .thenReturn(
                Single.just(
                    Response.error(
                        401,
                        "Unauthorized".toResponseBody("application/json".toMediaTypeOrNull())
                    )
                )
            )
        val login = userRepository.login("text", "text").blockingGet()
        assert(login.code == 401)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun testRefreshTokenOkResponse() {
        userRepository = UserRepositoryImpl(userAPI, repository)
        val loginResponse = LoginResponse("", "", 1, "")
        Mockito.`when`(userAPI.refreshToken(anyString(), anyString(), anyString()))
            .thenReturn(Single.just(Response.success(loginResponse)))
        val refresh = userRepository.refreshToken("text").blockingGet()
        assert(refresh)
        verify(repository, times(1)).saveToken(anyString())
        verify(repository, times(1)).saveRefreshToken(anyString())
    }

    @Test
    fun testRefreshTokenUnauthorizedResponse() {
        userRepository = UserRepositoryImpl(userAPI, repository)
        val loginResponse = LoginResponse("", "", 1, "")
        Mockito.`when`(userAPI.refreshToken(anyString(), anyString(), anyString()))
            .thenReturn(
                Single.just(
                    Response.error(
                        401,
                        "Unauthorized".toResponseBody("application/json".toMediaTypeOrNull())
                    )
                )
            )
        val refresh = userRepository.refreshToken("text").blockingGet()
        assert(refresh == false)
        verifyNoMoreInteractions(repository)
    }
}