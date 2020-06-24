package com.bookstore.repository

import com.bookstore.dao.remote.RemoteTransactionDAO
import com.bookstore.model.request.transaction.CheckoutRequest
import com.bookstore.model.request.transaction.PaymentRequest
import com.bookstore.model.response.transaction.Transaction
import com.bookstore.utils.SessionHelper
import com.bookstore.utils.SessionHelper.asBearer

class TransactionRepository(
    private val userRepository: UserRepository,
    private val transactionDAO: RemoteTransactionDAO
) {

    suspend fun performCheckout(checkoutRequest: CheckoutRequest) : Transaction =
        userRepository.checkSession().let {
            if (it != null) return transactionDAO.performCheckout(it.asBearer(), checkoutRequest)
            else throw SessionHelper.unauthorizedException
        }

    suspend fun performPayment(paymentRequest: PaymentRequest): Transaction =
        userRepository.checkSession().let {
            if (it != null) return transactionDAO.performPayment(it.asBearer(), paymentRequest)
            else throw SessionHelper.unauthorizedException
        }

    suspend fun getCheckoutHistory(): List<Transaction> = userRepository.checkSession().let {
        if (it != null) return transactionDAO.getCheckoutHistory(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }
}