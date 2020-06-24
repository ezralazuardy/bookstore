package com.bookstore.ui.purchase.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.formatted.transaction.PurchaseHistoryResponse
import com.bookstore.repository.TransactionRepository
import com.bookstore.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PurchaseHistoryViewModel(
    application: Application,
    private val transactionRepository: TransactionRepository
) : AndroidViewModel(application) {

    private val _purchaseHistoryResponse = MutableLiveData<PurchaseHistoryResponse>()
    val purchaseHistoryResponse: LiveData<PurchaseHistoryResponse> = _purchaseHistoryResponse

    fun getPurchaseHistory() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = transactionRepository.getCheckoutHistory().sortedByDescending { it.id }
            if (result.isNotEmpty()) _purchaseHistoryResponse.postValue(
                PurchaseHistoryResponse(
                    RetrofitStatus.SUCCESS,
                    result
                )
            )
            else {
                _purchaseHistoryResponse.postValue(PurchaseHistoryResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _purchaseHistoryResponse.postValue(PurchaseHistoryResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _purchaseHistoryResponse.postValue(PurchaseHistoryResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}