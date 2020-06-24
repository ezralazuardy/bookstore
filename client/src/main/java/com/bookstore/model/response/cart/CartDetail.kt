package com.bookstore.model.response.cart

import android.os.Parcelable
import com.bookstore.model.response.book.Book
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDetail(
    val bookModel: Book,
    val cartDetailStatus: String,
    val createdBy: String?,
    val createdTime: String,
    val id: Int,
    val updatedBy: String?,
    val updatedTime: String
) : Parcelable