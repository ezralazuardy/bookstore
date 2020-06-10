package com.bookstore.ui.book

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.CartStatus
import com.bookstore.model.formatted.book.FavouriteBookResponse
import com.bookstore.model.formatted.cart.CartResponse
import com.bookstore.model.request.book.FavouriteBookRequest
import com.bookstore.model.request.cart.CartRequest
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.repository.BookRepository
import com.bookstore.repository.CartRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookDetailViewModel(
    application: Application,
    private val cartRepository: CartRepository,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

    private val _cartResponse = MutableLiveData<CartResponse>()
    val cartResponse: LiveData<CartResponse> = _cartResponse
    var cartAdded = false

    fun getCart(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = cartRepository.getCart()
            result.details.firstOrNull { it.bookModel.id == bookId }?.let{
                if(it.cartDetailStatus == CartStatus.CARTED.toString()) cartAdded = true
            }
            _cartResponse.postValue(CartResponse(RetrofitStatus.SUCCESS, result))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _cartResponse.postValue(CartResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _cartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _addCartResponse = MutableLiveData<CartResponse>()
    val addCartResponse: LiveData<CartResponse> = _addCartResponse

    fun addBookToCart(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val cartRequest = CartRequest(bookId)
            val result = cartRepository.addBookToCart(cartRequest)
            if(result.isSuccessful){
                cartAdded = true
                _addCartResponse.postValue(CartResponse(RetrofitStatus.SUCCESS))
            } else {
                _addCartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _addCartResponse.postValue(CartResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _addCartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _removeCartResponse = MutableLiveData<CartResponse>()
    val removeCartResponse: LiveData<CartResponse> = _removeCartResponse

    fun removeBookFromCart(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val cart = cartRepository.getCart()
            cart.details.firstOrNull { it.bookModel.id == bookId }?.id.let { detailId ->
                if(detailId != null) {
                    val result = cartRepository.removeBookFromCart(detailId)
                    if(result.isSuccessful) {
                        cartAdded = false
                        _removeCartResponse.postValue(CartResponse(RetrofitStatus.SUCCESS))
                    } else  {
                        _removeCartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
                        Log.e(this::class.java.simpleName, result.toString())
                    }
                } else {
                    _removeCartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
                    Log.e(this::class.java.simpleName, "Can't find book id: $bookId in cart data")
                }
            }
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _removeCartResponse.postValue(CartResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _removeCartResponse.postValue(CartResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _favouriteBookResponse = MutableLiveData<FavouriteBookResponse>()
    val favouriteBookResponse: LiveData<FavouriteBookResponse> = _favouriteBookResponse
    var favouriteBookAdded = false

    fun getFavouriteBook(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = bookRepository.getFavouriteBook()
            if(result.details.firstOrNull { it.bookModel.id == bookId } != null) favouriteBookAdded = true
            _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.SUCCESS, result))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _addFavouriteBookResponse = MutableLiveData<FavouriteBookResponse>()
    val addFavouriteBookResponse: LiveData<FavouriteBookResponse> = _addFavouriteBookResponse

    fun addBookToFavourite(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val favouriteBookRequest = FavouriteBookRequest(bookId)
            val result = bookRepository.addBookToFavourite(favouriteBookRequest)
            if(result.isSuccessful) {
                favouriteBookAdded = true
                _addFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.SUCCESS))
            } else {
                _addFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _addFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _addFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _removeFavouriteBookResponse = MutableLiveData<FavouriteBookResponse>()
    val removeFavouriteBookResponse: LiveData<FavouriteBookResponse> = _removeFavouriteBookResponse

    fun removeBookFromFavourite(bookId: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val favourites = bookRepository.getFavouriteBook()
            favourites.details.firstOrNull { it.bookModel.id == bookId }?.id.let { detailId ->
                if(detailId != null) {
                    val result = bookRepository.removeBookFromFavourite(detailId)
                    if(result.isSuccessful) {
                        favouriteBookAdded = false
                        _removeFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.SUCCESS))
                    } else {
                        _removeFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
                        Log.e(this::class.java.simpleName, result.toString())
                    }
                } else {
                    _removeFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
                    Log.e(this::class.java.simpleName, "Can't find book id: $bookId in favourite data")
                }
            }
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _removeFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _removeFavouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}