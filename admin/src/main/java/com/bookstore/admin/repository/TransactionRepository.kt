package com.bookstore.admin.repository

import com.bookstore.admin.dao.remote.RemoteTransactionDAO
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
}