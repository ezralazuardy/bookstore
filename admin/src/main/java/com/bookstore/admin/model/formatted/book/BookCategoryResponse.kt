package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.response.book.BookCategory
import com.bookstore.admin.model.status.RetrofitStatus

data class BookCategoryResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val list: List<BookCategory>? = null
)