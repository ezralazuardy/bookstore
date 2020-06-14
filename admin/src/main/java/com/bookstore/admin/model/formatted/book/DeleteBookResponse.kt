package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.status.RetrofitStatus

data class DeleteBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)