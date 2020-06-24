package com.bookstore.ui.purchase.history.adapter

import com.bookstore.model.response.transaction.Transaction

interface PurchaseHistoryItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(transaction: Transaction)
}