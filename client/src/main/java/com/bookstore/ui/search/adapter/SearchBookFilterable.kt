package com.bookstore.ui.search.adapter

import com.bookstore.constant.BookType

interface SearchBookFilterable {

    fun performFilterByName(bookName: String?, bookTypes: List<BookType>)

    fun performFilterByType(bookTypes: List<BookType>)
}