package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.response.book.FavouriteBook
import com.bookstore.admin.model.status.RetrofitStatus

data class FavouriteBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val favouriteBook: FavouriteBook? = null
)