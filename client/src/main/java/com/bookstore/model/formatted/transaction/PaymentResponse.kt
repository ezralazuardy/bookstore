package com.bookstore.model.formatted.transaction

import com.bookstore.model.response.transaction.Transaction
import com.bookstore.model.status.RetrofitStatus

data class PaymentResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)