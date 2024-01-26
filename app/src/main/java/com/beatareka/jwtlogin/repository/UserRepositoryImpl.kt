package com.beatareka.jwtlogin.repository

import com.beatareka.jwtlogin.network.UserAPI
import com.beatareka.jwtlogin.network.dto.BaseResponse
import com.beatareka.jwtlogin.network.dto.LoginResponse
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userAPI: UserAPI,
    private val dataStoreRepository: TokenStoreRepository
) : UserRepository {

    companion object {
        const val clientId: String = "69bfdce9-2c9f-4a12-aa7b-4fe15e1228dc"
        const val refresh_type: String = "refresh_token"
        const val password_type: String = "password"
    }

    override fun login(username: String, password: String): Single<BaseResponse> {
        return userAPI.login(username, password, password_type, clientId)
            .map { response ->
                if (response.isSuccessful) {
                    saveJWTFromLoginResponse(response)
                }
                return@map BaseResponse(response.code(), response.message())
            }
    }

    override fun refreshToken(refreshToken: String): Single<Boolean> {
        return userAPI.refreshToken(refreshToken, refresh_type, clientId)
            .map { response ->
                if (response.isSuccessful) {
                    saveJWTFromLoginResponse(response)
                    return@map true
                } else {
                    return@map false
                }
            }
    }

    private fun saveJWTFromLoginResponse(response: Response<LoginResponse>) {
        response.body()?.accessToken?.let { dataStoreRepository.saveToken(it) }
        response.body()?.accessToken?.let { dataStoreRepository.saveRefreshToken(it) }
    }
}