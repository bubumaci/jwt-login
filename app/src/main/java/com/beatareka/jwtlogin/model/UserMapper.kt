package com.beatareka.jwtlogin.model

import com.google.gson.Gson
import java.util.Base64
import javax.inject.Inject

class UserMapper @Inject constructor(private val gson: Gson) {
    fun mapToken(token: String): User {
        val payload = extractJwtPayload(token)
        val user: User = gson.fromJson(payload, User::class.java)
        return user
    }

    private fun extractJwtPayload(jwt: String): String {
        val parts = jwt.split(".")
        return try {
            val charset = charset("UTF-8")
            val payload =
                String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            return payload
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }
}