package com.bookstore.admin.model.formatted.transaction

import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.transaction.Transaction

data class CheckoutResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transaction: Transaction? = null
)