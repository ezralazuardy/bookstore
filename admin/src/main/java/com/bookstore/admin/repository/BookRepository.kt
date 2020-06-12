package com.bookstore.admin.repository

import com.bookstore.admin.dao.remote.RemoteBookDAO
import com.bookstore.admin.model.request.book.FavouriteBookRequest
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.response.book.FavouriteBook
import com.bookstore.admin.utils.SessionHelper
import com.bookstore.admin.utils.SessionHelper.asBearer
import okhttp3.ResponseBody
import retrofit2.Response

class BookRepository(
    private val userRepository: UserRepository,
    private val bookDAO: RemoteBookDAO
) {

    suspend fun getBook(): List<Book> = userRepository.checkSession().let {
        if (it != null) return bookDAO.getBook(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }

    suspend fun getFavouriteBook(): FavouriteBook = userRepository.checkSession().let {
        if (it != null) return bookDAO.getFavouriteBook(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }

    suspend fun addBookToFavourite(favouriteBookRequest: FavouriteBookRequest): Response<ResponseBody> =
        userRepository.checkSession().let {
            if (it != null) return bookDAO.addBookToFavourite(it.asBearer(), favouriteBookRequest)
            else throw SessionHelper.unauthorizedException
        }

    suspend fun removeBookFromFavourite(detailId: Int): Response<ResponseBody> =
        userRepository.checkSession().let {
            if (it != null) return bookDAO.removeBookFromFavourite(it.asBearer(), detailId)
            else throw SessionHelper.unauthorizedException
        }
}