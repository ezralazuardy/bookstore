package com.bookstore.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.BookStatus
import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.formatted.book.SearchBookResponse
import com.bookstore.repository.BookRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchBookViewModel(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

    private val _searchBookResponse = MutableLiveData<SearchBookResponse>()
    val searchBookResponse: LiveData<SearchBookResponse> = _searchBookResponse

    fun getBook() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = bookRepository.getBook().sortedBy { it.id }
                .filter { it.bookStatus == BookStatus.FOR_SELL.toString() }
            if (result.isNotEmpty()) _searchBookResponse.postValue(
                SearchBookResponse(
                    RetrofitStatus.SUCCESS, result
                )
            )
            else {
                _searchBookResponse.postValue(SearchBookResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch(throwable: Throwable) {
            if(throwable is HttpException && throwable.code() == 401)
                _searchBookResponse.postValue(SearchBookResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _searchBookResponse.postValue(SearchBookResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}