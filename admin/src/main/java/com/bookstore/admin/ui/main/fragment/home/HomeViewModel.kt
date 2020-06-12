package com.bookstore.admin.ui.main.fragment.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.admin.constant.BookStatus
import com.bookstore.admin.model.formatted.book.BookCategoryCountResponse
import com.bookstore.admin.model.formatted.book.BookCountResponse
import com.bookstore.admin.model.formatted.transaction.PurchaseCountResponse
import com.bookstore.admin.model.status.RetrofitStatus
import com.bookstore.admin.repository.BookRepository
import com.bookstore.admin.repository.TransactionRepository
import com.bookstore.admin.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    application: Application,
    private val bookRepository: BookRepository,
    private val transactionRepository: TransactionRepository
) : AndroidViewModel(application) {

    private val _bookCountResponse = MutableLiveData<BookCountResponse>()
    val bookCountResponse: LiveData<BookCountResponse> = _bookCountResponse

    fun getBookCount() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result =
                bookRepository.getBook().filter { it.bookStatus == BookStatus.FOR_SELL.toString() }
            if (result.isNotEmpty()) _bookCountResponse.postValue(
                BookCountResponse(
                    RetrofitStatus.SUCCESS,
                    result.count()
                )
            )
            else {
                _bookCountResponse.postValue(BookCountResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _bookCountResponse.postValue(BookCountResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _bookCountResponse.postValue(BookCountResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _bookCategoryCountResponse = MutableLiveData<BookCategoryCountResponse>()
    val bookCategoryCountResponse: LiveData<BookCategoryCountResponse> = _bookCategoryCountResponse

    fun getBookCategoryCount() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = bookRepository.getBookCategory()
            if (result.isNotEmpty()) _bookCategoryCountResponse.postValue(
                BookCategoryCountResponse(
                    RetrofitStatus.SUCCESS,
                    result.count()
                )
            )
            else {
                _bookCategoryCountResponse.postValue(BookCategoryCountResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _bookCategoryCountResponse.postValue(BookCategoryCountResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _bookCategoryCountResponse.postValue(BookCategoryCountResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }

    private val _purchaseCountResponse = MutableLiveData<PurchaseCountResponse>()
    val purchaseCountResponse: LiveData<PurchaseCountResponse> = _purchaseCountResponse

    fun getPurchaseCount() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = transactionRepository.getCheckoutHistory()
            if (result.isNotEmpty()) _purchaseCountResponse.postValue(
                PurchaseCountResponse(
                    RetrofitStatus.SUCCESS,
                    result.count()
                )
            )
            else {
                _purchaseCountResponse.postValue(PurchaseCountResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _purchaseCountResponse.postValue(PurchaseCountResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _purchaseCountResponse.postValue(PurchaseCountResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}