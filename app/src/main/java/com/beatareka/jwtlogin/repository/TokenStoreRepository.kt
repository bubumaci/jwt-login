package com.beatareka.jwtlogin.repository

interface TokenStoreRepository {
    fun saveToken(value: String)
    fun saveRefreshToken(value: String)
    suspend fun getToken(): String?
    suspend fun getRefreshToken(): String?
}