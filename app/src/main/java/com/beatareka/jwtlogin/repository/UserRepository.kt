package com.beatareka.jwtlogin.repository

import com.beatareka.jwtlogin.network.dto.BaseResponse
import io.reactivex.Single

interface UserRepository {
    fun login(username: String, password: String): Single<BaseResponse>
    fun refreshToken(refreshToken: String): Single<Boolean>
}
