package com.bookstore.model.formatted.book

import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.book.Book

data class SearchBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val list: List<Book>? = null
)