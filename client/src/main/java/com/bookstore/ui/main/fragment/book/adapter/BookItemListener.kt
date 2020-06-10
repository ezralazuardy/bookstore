package com.bookstore.ui.main.fragment.book.adapter

import com.bookstore.model.response.book.Book

interface BookItemListener {

    fun onItemClick(book: Book)
}