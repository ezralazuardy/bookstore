package com.bookstore.ui.purchase.detail.adapter

import com.bookstore.model.response.transaction.TransactionDetail

interface PurchaseItemListListener {

    fun onItemClick(transactionDetail: TransactionDetail)
}