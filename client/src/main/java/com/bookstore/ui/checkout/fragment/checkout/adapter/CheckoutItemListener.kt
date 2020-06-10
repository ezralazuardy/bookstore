package com.bookstore.ui.checkout.fragment.checkout.adapter

import com.bookstore.model.response.cart.CartDetail

interface CheckoutItemListener {

    fun onItemClick(cartDetail: CartDetail)

    fun onItemDraw(cartDetails: List<CartDetail>)
}