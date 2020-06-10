package com.bookstore.ui.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.PaymentStatus
import com.bookstore.model.formatted.cart.CartResponse
import com.bookstore.model.formatted.transaction.CheckoutResponse
import com.bookstore.model.formatted.transaction.PaymentResponse
import com.bookstore.model.request.transaction.CheckoutRequest
import com.bookstore.model.request.transaction.PaymentRequest
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.repository.CartRepository
import com.bookstore.repository.TransactionRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheckoutViewModel(
    application: Application,
    private val cartRepository: CartRepository,
    private val transactionRepository: TransactionRepository
) : AndroidViewModel(application) {

    private val _cartResponse = MutableLiveData<CartResponse>()
    val cartResponse: LiveData<CartResponse> = _cartResponse

    fun getCart() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = cartRepository.getCart()
            if(result.details.isNotEmpty())
                _cartResponse.postValue(CartResponse(RetrofitStatus.SUCCESS, result))
            else
                _cartResponse.postValue(CartResponse(RetrofitStatus.EMPTY))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _cartResponse.postValue(CartResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _cartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _checkoutResponse = MutableLiveData<CheckoutResponse>()
    val checkoutResponse: LiveData<CheckoutResponse> = _checkoutResponse

    fun performCheckout(cartDetailIds: List<Int>) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val checkoutRequest = CheckoutRequest(cartDetailIds)
            val result = transactionRepository.performCheckout(checkoutRequest)
            _checkoutResponse.postValue(CheckoutResponse(RetrofitStatus.SUCCESS, result))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _checkoutResponse.postValue(CheckoutResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _checkoutResponse.postValue(CheckoutResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _paymentResponse = MutableLiveData<PaymentResponse>()
    val paymentResponse: LiveData<PaymentResponse> = _paymentResponse

    fun performPayment(transactionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val paymentRequest = PaymentRequest(transactionId, PaymentStatus.SETTLED.toString())
            val result = transactionRepository.performPayment(paymentRequest)
            _paymentResponse.postValue(PaymentResponse(RetrofitStatus.SUCCESS, result))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _paymentResponse.postValue(PaymentResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _paymentResponse.postValue(PaymentResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}