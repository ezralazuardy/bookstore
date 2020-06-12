package com.bookstore.model.formatted.book

import com.bookstore.model.response.book.FavouriteBook
import com.bookstore.model.status.RetrofitStatus

data class FavouriteBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val favouriteBook: FavouriteBook? = null
)