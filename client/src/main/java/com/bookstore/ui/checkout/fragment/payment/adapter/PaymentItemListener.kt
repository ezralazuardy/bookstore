package com.bookstore.ui.checkout.fragment.payment.adapter

import com.bookstore.model.response.transaction.TransactionDetail

interface PaymentItemListener {

    fun onItemClick(transactionDetail: TransactionDetail)
}