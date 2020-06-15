package com.bookstore.admin.ui.purchase.adapter

import com.bookstore.admin.model.response.transaction.TransactionDetail

interface PurchaseItemListListener {

    fun onItemClick(transactionDetail: TransactionDetail)
}