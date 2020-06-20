package com.bookstore.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.formatted.book.FavouriteBookResponse
import com.bookstore.repository.BookRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WishlistViewModel(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

    private val _favouriteBookResponse = MutableLiveData<FavouriteBookResponse>()
    val favouriteResponse: LiveData<FavouriteBookResponse> = _favouriteBookResponse

    fun getFavouriteBook() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = bookRepository.getFavouriteBook()
            if(result.details.isNotEmpty())
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.SUCCESS, result))
            else
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.EMPTY))
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _favouriteBookResponse.postValue(FavouriteBookResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}