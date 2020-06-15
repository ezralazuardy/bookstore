package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus

data class AddBookCategoryResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)