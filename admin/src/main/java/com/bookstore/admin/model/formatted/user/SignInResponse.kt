package com.bookstore.admin.model.formatted.user

import com.bookstore.admin.constant.RetrofitStatus

data class SignInResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)