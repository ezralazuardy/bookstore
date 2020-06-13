package com.bookstore.admin.model.formatted.transaction

import com.bookstore.admin.model.response.transaction.Transaction
import com.bookstore.admin.model.status.RetrofitStatus

data class PurchaseListResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val transactions: List<Transaction>? = null
)