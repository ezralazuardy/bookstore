package com.bookstore.model.formatted.user

import com.bookstore.model.status.RetrofitStatus

data class SignInResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)