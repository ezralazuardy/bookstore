package com.bookstore.admin.dao.remote

import com.bookstore.admin.config.AppConfig
import com.bookstore.admin.model.response.transaction.Transaction
import retrofit2.http.GET
import retrofit2.http.Header

interface RemoteTransactionDAO {

    @GET("/api/rest/transaction/findByUserId/${AppConfig.OAUTH_DEFAULT_CUSTOMER_ID}")
    suspend fun getCheckoutHistory(@Header("Authorization") authorization: String): List<Transaction>
}