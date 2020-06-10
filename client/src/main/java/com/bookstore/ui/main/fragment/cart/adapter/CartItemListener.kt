package com.bookstore.ui.main.fragment.cart.adapter

import com.bookstore.model.response.cart.CartDetail

interface CartItemListener {

    fun onItemSearch(empty: Boolean)

    fun onItemClick(cartDetail: CartDetail)

    fun onItemRemove(position: Int, cartDetail: CartDetail)

    fun onItemDraw(cartDetails: List<CartDetail>)
}