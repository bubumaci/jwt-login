package com.beatareka.jwtlogin.viewmodel;

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beatareka.jwtlogin.network.dto.BaseResponse
import com.beatareka.jwtlogin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: UserRepository
) : ViewModel() {

    val password = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    private val _canSend = MutableLiveData<Boolean>()
    val canSend: LiveData<Boolean> = _canSend

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val loggedIn = MutableLiveData<Boolean>()

    private var disposable: CompositeDisposable?

    init {
        disposable = CompositeDisposable()
        loading.value = false
    }

    fun checkCanSend(text: Editable) {
        _canSend.value = !(username.value.isNullOrBlank() || password.value.isNullOrBlank())
    }

    fun login(): MutableLiveData<BaseResponse> {
        val response = MutableLiveData<BaseResponse>()
        loading.value = true
        disposable?.add(
            api.login(username.value.toString(), password.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BaseResponse>() {
                    override fun onSuccess(value: BaseResponse) {
                        response.value = value
                        loggedIn.value = when (value.code) {
                            200 -> true
                            401 -> {
                                error.value = "Invalid username or password."
                                false
                            }

                            else -> {
                                error.value = "Unexpected error."
                                false
                            }
                        }
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        error.value = "Unexpected error."
                        loading.value = false
                    }
                })
        )
        return response
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
        disposable = null
    }

}
