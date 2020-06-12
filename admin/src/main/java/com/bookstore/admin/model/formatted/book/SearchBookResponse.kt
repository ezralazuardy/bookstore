package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.status.RetrofitStatus

data class SearchBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val list: List<Book>? = null
)