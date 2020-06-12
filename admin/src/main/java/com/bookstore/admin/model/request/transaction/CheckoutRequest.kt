package com.bookstore.admin.model.request.transaction

import com.bookstore.admin.config.AppConfig

data class CheckoutRequest(
    val cartDetailIds: List<Int>,
    val userId: Int = AppConfig.OAUTH_DEFAULT_USER_ID
)