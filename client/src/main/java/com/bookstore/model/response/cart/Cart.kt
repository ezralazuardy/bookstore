package com.bookstore.model.response.cart

import android.os.Parcelable
import com.bookstore.model.response.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val createdBy: String?,
    val createdTime: String,
    val details: List<CartDetail>,
    val id: Int,
    val updatedBy: String?,
    val updatedTime: String,
    val userModel: User
) : Parcelable