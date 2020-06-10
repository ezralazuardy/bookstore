package com.bookstore.model.formatted.book

import com.bookstore.model.response.book.Book
import com.bookstore.model.status.RetrofitStatus

data class BookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val list: List<Book>? = null
)