package com.bookstore.admin.ui.main.fragment.purchase.adapter

import com.bookstore.admin.model.response.transaction.Transaction

interface PurchaseItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(transaction: Transaction)

    fun onItemDraw(transactions: List<Transaction>)
}