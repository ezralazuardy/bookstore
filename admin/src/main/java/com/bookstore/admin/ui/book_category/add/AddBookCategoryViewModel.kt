package com.bookstore.admin.ui.book_category.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.formatted.book.AddBookCategoryResponse
import com.bookstore.admin.model.request.book.AddBookCategoryRequest
import com.bookstore.admin.repository.BookRepository
import com.bookstore.admin.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddBookCategoryViewModel(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

    private val _addBookCategoryResponse = MutableLiveData<AddBookCategoryResponse>()
    val addBookCategoryResponse: LiveData<AddBookCategoryResponse> = _addBookCategoryResponse

    fun addBookCategory(addBookCategoryRequest: AddBookCategoryRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = bookRepository.addBookCategory(addBookCategoryRequest)
                _addBookCategoryResponse.postValue(
                    AddBookCategoryResponse(
                        RetrofitStatus.SUCCESS,
                        result
                    )
                )
            } catch (throwable: Throwable) {
                if (throwable is HttpException && throwable.code() == 401)
                    _addBookCategoryResponse.postValue(AddBookCategoryResponse(RetrofitStatus.UNAUTHORIZED))
                else
                    _addBookCategoryResponse.postValue(AddBookCategoryResponse(RetrofitStatus.FAILURE))
                throwable.printRetrofitError()
            }
        }
}