package com.bookstore.admin.model.formatted.cart

import com.bookstore.admin.model.response.cart.Cart
import com.bookstore.admin.model.status.RetrofitStatus

data class CartResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val cart: Cart? = null
)