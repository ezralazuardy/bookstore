package com.bookstore.admin.ui.main.fragment.purchase

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.formatted.transaction.PurchaseListResponse
import com.bookstore.admin.repository.TransactionRepository
import com.bookstore.admin.utils.Retrofit.printRetrofitError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PurchaseViewModel(
    application: Application,
    private val transactionRepository: TransactionRepository
) : AndroidViewModel(application) {

    private val _purchaseListResponse = MutableLiveData<PurchaseListResponse>()
    val purchaseListResponse: LiveData<PurchaseListResponse> = _purchaseListResponse

    fun getPurchaseList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = transactionRepository.getCheckoutHistory().sortedByDescending { it.id }
            if (result.isNotEmpty()) _purchaseListResponse.postValue(
                PurchaseListResponse(
                    RetrofitStatus.SUCCESS,
                    result
                )
            )
            else {
                _purchaseListResponse.postValue(PurchaseListResponse(RetrofitStatus.EMPTY))
                Log.e(this::class.java.simpleName, result.toString())
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401)
                _purchaseListResponse.postValue(PurchaseListResponse(RetrofitStatus.UNAUTHORIZED))
            else
                _purchaseListResponse.postValue(PurchaseListResponse(RetrofitStatus.FAILURE))
            throwable.printRetrofitError()
        }
    }
}