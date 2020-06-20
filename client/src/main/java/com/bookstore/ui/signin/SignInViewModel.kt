package com.bookstore.ui.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.formatted.user.SignInResponse
import com.bookstore.model.request.user.AccessTokenRequest
import com.bookstore.repository.UserRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val _signInResponse = MutableLiveData<SignInResponse>()
    val signInResponse: LiveData<SignInResponse> = _signInResponse

    fun signIn(username: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val accessTokenRequest = AccessTokenRequest(username = username, password = password)
            userRepository.getAccessToken(accessTokenRequest).run {
                userRepository.saveSession(this)
                _signInResponse.postValue(SignInResponse(RetrofitStatus.SUCCESS))
            }
        } catch (throwable: Throwable) {
            _signInResponse.postValue(SignInResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}