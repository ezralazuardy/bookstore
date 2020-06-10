package com.bookstore.model.formatted.cart

import com.bookstore.model.response.cart.Cart
import com.bookstore.model.status.RetrofitStatus

data class CartResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val cart: Cart? = null
)