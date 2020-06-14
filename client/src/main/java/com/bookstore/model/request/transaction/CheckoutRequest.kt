package com.bookstore.model.request.transaction

import com.bookstore.config.AppConfig

data class CheckoutRequest(
    val cartDetailIds: List<Int>,
    val userId: Int = AppConfig.OAUTH_DEFAULT_CUSTOMER_ID
)