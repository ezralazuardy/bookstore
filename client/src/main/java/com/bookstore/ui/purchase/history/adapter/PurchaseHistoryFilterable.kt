package com.bookstore.ui.purchase.history.adapter

interface PurchaseHistoryFilterable {

    fun performFilterByName(invoiceNumber: String?)
}