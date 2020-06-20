package com.bookstore.model.formatted.transaction

import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.transaction.Transaction

data class CheckoutResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)