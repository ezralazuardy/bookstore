package com.bookstore.admin.model.request.book

import com.bookstore.admin.config.AppConfig

data class FavouriteBookRequest(
    val bookId: Int,
    val userId: Int = AppConfig.OAUTH_DEFAULT_USER_ID
)