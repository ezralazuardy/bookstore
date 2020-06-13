package com.bookstore.admin.ui.main.fragment.book.adapter

import com.bookstore.admin.model.response.book.Book

interface BookItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(book: Book)

    fun onItemDraw(books: List<Book>)
}