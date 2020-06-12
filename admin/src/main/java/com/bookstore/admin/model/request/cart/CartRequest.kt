package com.bookstore.admin.model.request.cart

import com.bookstore.admin.config.AppConfig

data class CartRequest(
    val bookId: Int,
    val userId: Int = AppConfig.OAUTH_DEFAULT_USER_ID
)