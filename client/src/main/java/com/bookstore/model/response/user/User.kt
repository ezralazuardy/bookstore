package com.bookstore.model.response.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val address: String,
    val createdBy: String,
    val createdTime: String,
    val email: String,
    val fullName: String,
    val id: Int,
    val phoneNumber: String,
    val updatedBy: String,
    val updatedTime: String,
    val username: String
) : Parcelable