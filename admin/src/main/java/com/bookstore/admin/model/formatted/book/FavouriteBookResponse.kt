package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.book.FavouriteBook

data class FavouriteBookResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val favouriteBook: FavouriteBook? = null
)