package com.bookstore.model.response.book

import android.os.Parcelable
import com.bookstore.model.response.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavouriteBook(
    val createdBy: String,
    val createdTime: String,
    val details: List<FavouriteBookDetail>,
    val id: Int,
    val updatedBy: String,
    val updatedTime: String,
    val userModel: User
) : Parcelable