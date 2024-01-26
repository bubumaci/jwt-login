package com.beatareka.jwtlogin.network

import com.beatareka.jwtlogin.network.dto.LoginResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {
    @POST("idp/api/v1/token")
    @FormUrlEncoded
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
    ): Single<Response<LoginResponse>>

    @POST("idp/api/v1/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
    ): Single<Response<LoginResponse>>
}