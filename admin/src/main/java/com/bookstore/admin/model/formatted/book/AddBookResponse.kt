package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.book.Book

data class AddBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val addedBook: Book? = null
)