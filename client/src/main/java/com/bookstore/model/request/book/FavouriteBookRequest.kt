package com.bookstore.model.request.book

import com.bookstore.config.AppConfig

data class FavouriteBookRequest(
    val bookId: Int,
    val userId: Int = AppConfig.OAUTH_DEFAULT_CUSTOMER_ID
)