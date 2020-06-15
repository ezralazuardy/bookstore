package com.bookstore.admin.dao.remote

import com.bookstore.admin.model.request.book.AddBookCategoryRequest
import com.bookstore.admin.model.request.book.AddBookRequest
import com.bookstore.admin.model.request.book.UpdateBookCategoryRequest
import com.bookstore.admin.model.request.book.UpdateBookRequest
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.response.book.BookCategory
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RemoteBookDAO {

    @GET("/api/rest/book/findAll")
    suspend fun getBook(@Header("Authorization") authorization: String): List<Book>

    @POST("/api/rest/book/uploadImage/{book_id}")
    @Multipart
    suspend fun uploadBookImage(
        @Header("Authorization") authorization: String,
        @Path("book_id") bookId: Int,
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>

    @POST("/api/rest/book/save")
    suspend fun addBook(
        @Header("Authorization") authorization: String,
        @Body addBookRequest: AddBookRequest
    ): Book

    @POST("/api/rest/book/update")
    suspend fun updateBook(
        @Header("Authorization") authorization: String,
        @Body updateBookRequest: UpdateBookRequest
    ): Response<ResponseBody>

    @DELETE("/api/rest/book/deleteById/{book_id}")
    suspend fun deleteBook(
        @Header("Authorization") authorization: String,
        @Path("book_id") bookId: Int
    ): Response<ResponseBody>

    @GET("/api/rest/book-category/findAll")
    suspend fun getBookCategory(@Header("Authorization") authorization: String): List<BookCategory>

    @POST("/api/rest/book-category/save")
    suspend fun addBookCategory(
        @Header("Authorization") authorization: String,
        @Body addBookCategoryRequest: AddBookCategoryRequest
    ): Response<ResponseBody>

    @POST("/api/rest/book-category/update")
    suspend fun updateBookCategory(
        @Header("Authorization") authorization: String,
        @Body updateBookCategoryRequest: UpdateBookCategoryRequest
    ): Response<ResponseBody>

    @DELETE("/api/rest/book-category/deleteById/{book_category_id}")
    suspend fun deleteBookCategory(
        @Header("Authorization") authorization: String,
        @Path("book_category_id") bookCategoryId: Int
    ): Response<ResponseBody>
}