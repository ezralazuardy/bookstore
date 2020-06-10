package com.bookstore.model.formatted.transaction

import com.bookstore.model.response.transaction.Transaction
import com.bookstore.model.status.RetrofitStatus

data class CheckoutResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)