package com.bookstore.admin.model.response.transaction

import android.os.Parcelable
import com.bookstore.admin.model.response.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(
    val createdBy: String?,
    val createdTime: String,
    val details: List<TransactionDetail>,
    val id: Int,
    val invoiceNumber: String,
    val paymentMethod: String,
    val paymentTime: Long,
    val receiptImageUrl: String?,
    val transactionStatus: String,
    val updatedBy: String?,
    val updatedTime: String,
    val userModel: User
) : Parcelable