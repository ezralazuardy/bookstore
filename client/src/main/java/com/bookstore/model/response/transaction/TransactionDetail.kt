package com.bookstore.model.response.transaction

import android.os.Parcelable
import com.bookstore.model.response.book.Book
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionDetail(
    val bookModel: Book,
    val createdBy: String?,
    val createdTime: String,
    val id: Int,
    val price: Double,
    val updatedBy: String?,
    val updatedTime: String
) : Parcelable