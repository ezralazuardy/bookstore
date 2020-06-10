package com.bookstore.repository

import com.bookstore.dao.remote.RemoteCartDAO
import com.bookstore.model.request.cart.CartRequest
import com.bookstore.model.response.cart.Cart
import com.bookstore.utils.SessionHelper
import com.bookstore.utils.SessionHelper.asBearer
import okhttp3.ResponseBody
import retrofit2.Response

class CartRepository(
    private val userRepository: UserRepository,
    private val cartDAO: RemoteCartDAO
) {

    suspend fun getCart() : Cart = userRepository.checkSession().let {
        if(it != null) return cartDAO.getCart(it.asBearer())
        else throw SessionHelper.unauthorizedException
    }

    suspend fun addBookToCart(cartRequest: CartRequest) : Response<ResponseBody> =
        userRepository.checkSession().let {
            if(it != null) return cartDAO.addBookToCart(it.asBearer(), cartRequest)
            else throw SessionHelper.unauthorizedException
        }

    suspend fun removeBookFromCart(detailId: Int) : Response<ResponseBody> =
        userRepository.checkSession().let {
            if(it != null) return cartDAO.removeBookFromCart(it.asBearer(), detailId)
            else throw SessionHelper.unauthorizedException
        }
}