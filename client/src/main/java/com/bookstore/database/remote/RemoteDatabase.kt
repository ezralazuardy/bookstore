package com.bookstore.database.remote

import com.bookstore.dao.remote.RemoteBookDAO
import com.bookstore.dao.remote.RemoteCartDAO
import com.bookstore.dao.remote.RemoteTransactionDAO
import com.bookstore.dao.remote.RemoteUserDAO
import com.bookstore.utils.Retrofit

object RemoteDatabase {

    val userDAO: RemoteUserDAO by lazy {
        Retrofit.getClient().create(RemoteUserDAO::class.java)
    }

    val bookDAO: RemoteBookDAO by lazy {
        Retrofit.getClient().create(RemoteBookDAO::class.java)
    }

    val cartDAO: RemoteCartDAO by lazy {
        Retrofit.getClient().create(RemoteCartDAO::class.java)
    }

    val transactionDAO: RemoteTransactionDAO by lazy {
        Retrofit.getClient().create(RemoteTransactionDAO::class.java)
    }
}