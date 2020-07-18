package com.bookstore.admin.model.response.book

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val authorName: String,
    val bookCategory: BookCategory,
    val bookCategoryId: Int,
    val bookStatus: String,
    val createdBy: String?,
    val createdTime: String?,
    val id: Int,
    val imageUrl: String?,
    val isbn: String?,
    val price: Double,
    val publicationDate: String,
    val synopsis: String,
    val title: String,
    val updatedBy: String?,
    val updatedTime: String
) : Parcelable