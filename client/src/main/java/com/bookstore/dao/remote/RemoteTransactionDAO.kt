package com.bookstore.dao.remote

import com.bookstore.model.request.transaction.CheckoutRequest
import com.bookstore.model.request.transaction.PaymentRequest
import com.bookstore.model.response.transaction.Transaction
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RemoteTransactionDAO {

    @POST("/api/rest/transaction/checkout")
    suspend fun performCheckout(
        @Header("Authorization") authorization: String,
        @Body checkoutRequest: CheckoutRequest
    ) : Transaction

    @POST("/api/rest/transaction/payment")
    suspend fun performPayment(
        @Header("Authorization") authorization: String,
        @Body paymentRequest: PaymentRequest
    ) : Transaction
}