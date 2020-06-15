package com.bookstore.admin.model.request.book

data class UpdateBookCategoryRequest(
    val code: String,
    val id: Int,
    val name: String
)