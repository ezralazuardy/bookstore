package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.book.Book

data class SearchBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val list: List<Book>? = null
)