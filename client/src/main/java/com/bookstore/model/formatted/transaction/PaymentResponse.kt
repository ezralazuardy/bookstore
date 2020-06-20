package com.bookstore.model.formatted.transaction

import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.transaction.Transaction

data class PaymentResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)