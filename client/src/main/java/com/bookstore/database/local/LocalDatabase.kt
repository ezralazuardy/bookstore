package com.bookstore.database.local

import com.bookstore.dao.local.LocalUserDAO

object LocalDatabase {

    val userDAO: LocalUserDAO = LocalDatabaseImpl.database.userDAO
}