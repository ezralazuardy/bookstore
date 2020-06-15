package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus

data class UpdateBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)