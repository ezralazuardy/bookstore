package com.bookstore.ui.wishlist.adapter

import com.bookstore.model.response.book.Book

interface WishlistItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(book: Book)
}