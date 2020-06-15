package com.bookstore.admin.model.formatted.cart

import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.cart.Cart

data class CartResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN,
    val cart: Cart? = null
)