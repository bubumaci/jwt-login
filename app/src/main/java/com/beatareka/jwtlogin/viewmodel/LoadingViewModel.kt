package com.beatareka.jwtlogin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beatareka.jwtlogin.model.UserMapper
import com.beatareka.jwtlogin.repository.TokenStoreRepository
import com.beatareka.jwtlogin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: TokenStoreRepository,
    private val userMapper: UserMapper,
    private val userRepository: UserRepository
) : ViewModel() {

    val loggedIn = MutableLiveData<Boolean>()
    private val token = MutableLiveData<String>()
    private val refreshToken = MutableLiveData<String>()

    private var disposable: CompositeDisposable?

    init {
        disposable = CompositeDisposable()
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            token.value = repository.getToken()
            refreshToken.value = repository.getRefreshToken()
            val value = token.value
            if (value.isNullOrBlank()) {
                loggedIn.value = false
            } else {
                val user = userMapper.mapToken(value)
                if (isTokenDateExpired(user.exp)) {
                    refreshToken()
                } else {
                    loggedIn.value = false
                }
            }
        }
    }

    private fun isTokenDateExpired(exp: Long) =
        LocalDateTime.ofEpochSecond(exp, 0, ZoneOffset.UTC)
            .isAfter(LocalDateTime.now())

    private fun refreshToken() {
        disposable?.add(
            userRepository.refreshToken(refreshToken.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Boolean>() {
                    override fun onSuccess(value: Boolean) {
                        loggedIn.value = value
                    }

                    override fun onError(e: Throwable) {
                        loggedIn.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }

}
