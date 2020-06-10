package com.bookstore.model.formatted.book

import com.bookstore.model.status.RetrofitStatus
import com.bookstore.model.response.book.FavouriteBook

data class FavouriteBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val favouriteBook: FavouriteBook? = null
)