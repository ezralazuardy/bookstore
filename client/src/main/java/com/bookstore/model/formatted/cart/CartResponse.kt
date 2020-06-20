package com.bookstore.model.formatted.cart

import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.cart.Cart

data class CartResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val cart: Cart? = null
)