package com.bookstore.ui.purchase.history

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookstore.R
import com.bookstore.constant.RetrofitStatus
import com.bookstore.model.response.transaction.Transaction
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.purchase.detail.DetailPurchaseActivity
import com.bookstore.ui.purchase.history.adapter.PurchaseHistoryAdapter
import com.bookstore.ui.purchase.history.adapter.PurchaseHistoryItemListener
import com.bookstore.utils.ViewHelper.hide
import com.bookstore.utils.ViewHelper.hideKeyboard
import com.bookstore.utils.ViewHelper.show
import com.bookstore.utils.ViewHelper.showKeyboard
import kotlinx.android.synthetic.main.activity_purchase_history.*
import org.koin.android.ext.android.inject

class PurchaseHistoryActivity : AppCompatActivity(), PurchaseHistoryItemListener {

    private val mainViewModel: MainViewModel by inject()
    private val purchaseHistoryViewModel: PurchaseHistoryViewModel by inject()
    private val purchaseHistoryAdapter by lazy {
        PurchaseHistoryAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)
        purchaseHistoryViewModel.purchaseHistoryResponse.observe(this, Observer {
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when (it.status) {
                RetrofitStatus.SUCCESS -> it.transactions?.let { list ->
                    button_search.isEnabled = true
                    placeholder_empty.hide()
                    recyclerview.show()
                    purchaseHistoryAdapter.setData(list)
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> {
                    button_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty.show()
                }
            }

        })
        button_back.setOnClickListener {
            super.onBackPressed()
        }
        button_search.setOnClickListener {
            showSearchBar()
        }
        button_clear_search.setOnClickListener {
            hideSearchBar()
            purchaseHistoryAdapter.performFilterByName(null)
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                purchaseHistoryAdapter.performFilterByName(input_search.text.toString())
                input_search.hideKeyboard()
            }
            true
        }
        swipe_refresh_layout.setOnRefreshListener {
            purchaseHistoryViewModel.getPurchaseHistory()
        }
        recyclerview.apply {
            adapter = purchaseHistoryAdapter
            layoutManager = LinearLayoutManager(this@PurchaseHistoryActivity)
            setHasFixedSize(true)
        }
        button_search.isEnabled = false
        hideSearchBar()
    }

    override fun onResume() {
        super.onResume()
        if (input_search.text.toString().isEmpty()) purchaseHistoryViewModel.getPurchaseHistory()
    }

    override fun onItemSearch(empty: Boolean) {
        if (empty && !placeholder_empty.isShown) placeholder_empty_search.show()
        else placeholder_empty_search.hide()
    }

    override fun onItemClick(transaction: Transaction) {
        startActivity(
            Intent(this, DetailPurchaseActivity::class.java).putExtra(
                DetailPurchaseActivity.DATA,
                transaction
            )
        )
    }

    private fun hideSearchBar() {
        placeholder_empty.hide()
        input_search.hide()
        button_clear_search.hide()
        text_title_activity_purchase_history.show()
        button_search.show()
        input_search.apply {
            text.clear()
            hideKeyboard()
        }
    }

    private fun showSearchBar() {
        text_title_activity_purchase_history.hide()
        button_search.hide()
        input_search.show()
        button_clear_search.show()
        input_search.showKeyboard()
    }
}