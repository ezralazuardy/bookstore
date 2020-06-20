package com.bookstore.model.formatted.user

import com.bookstore.constant.RetrofitStatus

data class SignInResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)