package com.beatareka.jwtlogin.model;

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("idp:user_id")
    val userId: String,
    @SerializedName("idp:user_name")
    val userName: String,
    @SerializedName("idp:fullname")
    val fullName: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("exp")
    val exp: Long
)