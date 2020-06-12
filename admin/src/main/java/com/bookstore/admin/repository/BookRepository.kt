package com.bookstore.admin.repository

import com.bookstore.admin.dao.remote.RemoteBookDAO
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.response.book.BookCategory
import com.bookstore.admin.utils.SessionHelper
import com.bookstore.admin.utils.SessionHelper.asBearer

class BookRepository(
    private val userRepository: UserRepository,
    private val bookDAO: RemoteBookDAO
) {

    suspend fun getBook(): List<Book> = userRepository.checkSession().let {
        if (it != null) return bookDAO.getBook(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }

    suspend fun getBookCategory(): List<BookCategory> = userRepository.checkSession().let {
        if (it != null) return bookDAO.getBookCategory(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }
}