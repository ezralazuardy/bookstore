package com.bookstore.admin.database.local

import com.bookstore.admin.dao.local.LocalUserDAO

object LocalDatabase {

    val userDAO: LocalUserDAO = LocalDatabaseImpl.database.userDAO
}