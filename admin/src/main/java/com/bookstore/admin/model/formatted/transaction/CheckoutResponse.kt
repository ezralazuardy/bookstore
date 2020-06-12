package com.bookstore.admin.model.formatted.transaction

import com.bookstore.admin.model.response.transaction.Transaction
import com.bookstore.admin.model.status.RetrofitStatus

data class CheckoutResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)