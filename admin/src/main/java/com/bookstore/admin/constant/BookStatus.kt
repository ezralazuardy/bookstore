package com.bookstore.admin.constant

import java.util.*

enum class BookStatus {
    FOR_SELL, OUT_OF_STOCK, HIDE;

    fun toFormattedString() =
        super.toString().replace("_", " ").toUpperCase(Locale.getDefault()).trim()
}