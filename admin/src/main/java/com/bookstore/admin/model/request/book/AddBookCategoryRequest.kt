package com.bookstore.admin.model.request.book

data class AddBookCategoryRequest(
    val code: String,
    val name: String
)