package com.bookstore.ui.search.adapter

import com.bookstore.constant.BookType
import com.bookstore.model.response.book.Book

interface SearchBookItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(book: Book)

    fun onFilterByType(bookTypes: List<BookType>)
}