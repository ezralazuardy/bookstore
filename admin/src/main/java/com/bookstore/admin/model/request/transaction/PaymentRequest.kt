package com.bookstore.admin.model.request.transaction

data class PaymentRequest(
    val transactionId: Int,
    val transactionStatus: String,
    val receiptImageUrl: String = ""
)