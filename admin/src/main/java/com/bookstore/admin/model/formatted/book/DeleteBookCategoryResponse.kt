package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus

data class DeleteBookCategoryResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)