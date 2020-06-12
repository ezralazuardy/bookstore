package com.bookstore.admin.model.response.transaction

import android.os.Parcelable
import com.bookstore.admin.model.response.book.Book
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDetail(
    val bookModel: Book,
    val createdBy: String,
    val createdTime: String,
    val id: Int,
    val price: Double,
    val updatedBy: String,
    val updatedTime: String
) : Parcelable