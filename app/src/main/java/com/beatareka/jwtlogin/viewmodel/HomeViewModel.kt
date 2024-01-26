package com.beatareka.jwtlogin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beatareka.jwtlogin.model.User
import com.beatareka.jwtlogin.model.UserMapper
import com.beatareka.jwtlogin.repository.TokenStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TokenStoreRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private val token = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun findToken() {
        viewModelScope.launch {
            val tokenValue = repository.getToken()
            tokenValue?.let { token.value = it }
        }.invokeOnCompletion {
            val tokenValue = token.value
            if (!tokenValue.isNullOrBlank()) {
                val user = userMapper.mapToken(tokenValue)
                _user.value = user
            } else {
                error.value = "Unexpected error."
            }
        }

    }
}
