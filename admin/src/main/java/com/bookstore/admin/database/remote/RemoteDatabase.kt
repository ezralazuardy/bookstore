package com.bookstore.admin.database.remote

import com.bookstore.admin.dao.remote.RemoteBookDAO
import com.bookstore.admin.dao.remote.RemoteCartDAO
import com.bookstore.admin.dao.remote.RemoteTransactionDAO
import com.bookstore.admin.dao.remote.RemoteUserDAO
import com.bookstore.admin.utils.Retrofit

object RemoteDatabase {

    val userDAO: RemoteUserDAO = Retrofit.getClient().create(RemoteUserDAO::class.java)

    val bookDAO: RemoteBookDAO = Retrofit.getClient().create(RemoteBookDAO::class.java)

    val cartDAO: RemoteCartDAO = Retrofit.getClient().create(RemoteCartDAO::class.java)

    val transactionDAO: RemoteTransactionDAO =
        Retrofit.getClient().create(RemoteTransactionDAO::class.java)
}