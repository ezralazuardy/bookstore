package com.bookstore.admin.ui.main.fragment.book_category.adapter

import com.bookstore.admin.model.response.book.BookCategory

interface BookCategoryItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(bookCategory: BookCategory)

    fun onItemDraw(bookCategories: List<BookCategory>)
}