package com.bookstore.admin.dao.remote

import com.bookstore.admin.config.AppConfig
import com.bookstore.admin.model.request.book.FavouriteBookRequest
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.response.book.FavouriteBook
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RemoteBookDAO {

    @GET("/api/rest/book/findAll")
    suspend fun getBook(@Header("Authorization") authorization: String): List<Book>

    @GET("/api/rest/favourite-book/findByUserId/${AppConfig.OAUTH_DEFAULT_USER_ID}")
    suspend fun getFavouriteBook(@Header("Authorization") authorization: String): FavouriteBook

    @POST("/api/rest/favourite-book/saveOrUpdate")
    suspend fun addBookToFavourite(
        @Header("Authorization") authorization: String,
        @Body favouriteBookRequest: FavouriteBookRequest
    ): Response<ResponseBody>

    @DELETE("/api/rest/favourite-book/deleteByFavouriteBookDetailId/{detail_id}")
    suspend fun removeBookFromFavourite(
        @Header("Authorization") authorization: String,
        @Path("detail_id") detailId: Int
    ): Response<ResponseBody>
}