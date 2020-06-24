package com.bookstore.model.formatted.transaction

import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.transaction.Transaction

data class PurchaseHistoryResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transactions: List<Transaction>? = null
)