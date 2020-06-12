package com.bookstore.admin.repository

import com.bookstore.admin.dao.remote.RemoteTransactionDAO
import com.bookstore.admin.model.request.transaction.CheckoutRequest
import com.bookstore.admin.model.request.transaction.PaymentRequest
import com.bookstore.admin.model.response.transaction.Transaction
import com.bookstore.admin.utils.SessionHelper
import com.bookstore.admin.utils.SessionHelper.asBearer

class TransactionRepository(
    private val userRepository: UserRepository,
    private val transactionDAO: RemoteTransactionDAO
) {

    suspend fun getCheckoutHistory(): List<Transaction> = userRepository.checkSession().let {
        if (it != null) return transactionDAO.getCheckoutHistory(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }

    suspend fun performCheckout(checkoutRequest: CheckoutRequest): Transaction =
        userRepository.checkSession().let {
            if (it != null) return transactionDAO.performCheckout(it.asBearer(), checkoutRequest)
            else throw SessionHelper.unauthorizedException
        }

    suspend fun performPayment(paymentRequest: PaymentRequest): Transaction =
        userRepository.checkSession().let {
            if (it != null) return transactionDAO.performPayment(it.asBearer(), paymentRequest)
            else throw SessionHelper.unauthorizedException
        }
}