package com.bookstore.admin.ui.main.fragment.book_category.adapter

import com.bookstore.admin.model.response.book.BookCategory

interface BookCategoryItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemDraw(bookCategories: List<BookCategory>)

    fun onItemClick(bookCategory: BookCategory)

    fun onItemAdd(bookCategory: BookCategory)

    fun onItemUpdate(bookCategory: BookCategory)

    fun onItemDelete(bookCategory: BookCategory)
}