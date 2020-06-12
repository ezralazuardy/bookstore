package com.bookstore.admin.ui.main.fragment.book_category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.admin.model.formatted.book.BookCategoryResponse
import com.bookstore.admin.model.status.RetrofitStatus
import com.bookstore.admin.repository.BookRepository
import com.bookstore.admin.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookCategoryViewModel(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

    private val _bookCategoryResponse = MutableLiveData<BookCategoryResponse>()
    val bookCategoryResponse: LiveData<BookCategoryResponse> = _bookCategoryResponse

    fun getBookCategory() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = bookRepository.getBookCategory().sortedBy { it.id }
            if (result.isNotEmpty()) _bookCategoryResponse.postValue(
                BookCategoryResponse(
                    RetrofitStatus.SUCCESS,
                    result
                )
            )
            else {
                _bookCategoryResponse.postValue(BookCategoryResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _bookCategoryResponse.postValue(BookCategoryResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _bookCategoryResponse.postValue(BookCategoryResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}