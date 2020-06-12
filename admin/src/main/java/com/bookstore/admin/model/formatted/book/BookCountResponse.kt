package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.status.RetrofitStatus

data class BookCountResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val bookCount: Int = 0
)