package com.bookstore.admin.model.response.book

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavouriteBookDetail(
    val bookModel: Book,
    val createdBy: String,
    val createdTime: String,
    val id: Int,
    val updatedBy: String,
    val updatedTime: String
) : Parcelable