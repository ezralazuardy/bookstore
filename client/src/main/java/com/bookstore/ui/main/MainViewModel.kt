package com.bookstore.ui.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.model.formatted.user.SessionResponse
import com.bookstore.model.status.SessionStatus
import com.bookstore.repository.UserRepository
import com.bookstore.ui.signin.SignInActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val _sessionResponse = MutableLiveData<SessionResponse>()
    val sessionResponse: LiveData<SessionResponse> = _sessionResponse

    fun checkSession() = viewModelScope.launch(Dispatchers.IO) {
        userRepository.checkSession().let {
            if(it != null) _sessionResponse.postValue(SessionResponse(SessionStatus.AVAILABLE, it))
            else  _sessionResponse.postValue(SessionResponse(SessionStatus.UNAVAILABLE))
        }
    }

    fun logout(currentActivity: Activity) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.checkSession()?.let {
            userRepository.destroySession(it)
        }
        currentActivity.run {
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }
    }
}