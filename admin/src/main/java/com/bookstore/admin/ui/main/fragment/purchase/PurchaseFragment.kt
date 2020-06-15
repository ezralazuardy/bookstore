package com.bookstore.admin.ui.main.fragment.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookstore.admin.R
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.response.transaction.Transaction
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.ui.main.fragment.purchase.adapter.PurchaseAdapter
import com.bookstore.admin.ui.main.fragment.purchase.adapter.PurchaseItemListener
import com.bookstore.admin.ui.purchase.DetailPurchaseActivity
import com.bookstore.admin.utils.ViewHelper.hide
import com.bookstore.admin.utils.ViewHelper.hideKeyboard
import com.bookstore.admin.utils.ViewHelper.show
import com.bookstore.admin.utils.ViewHelper.showKeyboard
import kotlinx.android.synthetic.main.fragment_purchase.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PurchaseFragment : Fragment(), PurchaseItemListener {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val purchaseViewModel: PurchaseViewModel by viewModel()
    private val purchaseAdapter by lazy {
        PurchaseAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_purchase, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        purchaseViewModel.purchaseListResponse.observe(viewLifecycleOwner, Observer {
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when (it.status) {
                RetrofitStatus.SUCCESS -> it.transactions?.let { list ->
                    button_search.isEnabled = true
                    placeholder_empty.hide()
                    recyclerview.show()
                    purchaseAdapter.setData(list)
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> {
                    button_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty.show()
                    layout_purchase_count.hide()
                }
            }
        })
        button_search.setOnClickListener {
            showSearchBar()
        }
        button_clear_search.setOnClickListener {
            hideSearchBar()
            purchaseAdapter.performFilterByName(null)
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                purchaseAdapter.performFilterByName(input_search.text.toString())
                input_search.hideKeyboard()
            }
            true
        }
        swipe_refresh_layout.setOnRefreshListener {
            purchaseViewModel.getPurchaseList()
        }
        recyclerview.apply {
            adapter = purchaseAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        button_search.isEnabled = false
        hideSearchBar()
    }

    override fun onResume() {
        super.onResume()
        purchaseViewModel.getPurchaseList()
    }

    override fun onItemSearch(empty: Boolean) {
        if (empty && !placeholder_empty.isShown) placeholder_empty.show()
        else placeholder_empty.hide()
    }

    override fun onItemClick(transaction: Transaction) {
        startActivity(
            Intent(requireContext(), DetailPurchaseActivity::class.java).putExtra(
                DetailPurchaseActivity.DATA,
                transaction
            )
        )
    }

    override fun onItemDraw(transactions: List<Transaction>) {
        when {
            input_search.isShown -> layout_purchase_count.hide()
            transactions.isEmpty() -> layout_purchase_count.hide()
            else -> {
                text_purchase_count.text = transactions.count().toString()
                layout_purchase_count.show()
            }
        }
    }

    private fun hideSearchBar() {
        placeholder_empty.hide()
        input_search.hide()
        button_clear_search.hide()
        text_title_fragment_purchase.show()
        button_search.show()
        input_search.apply {
            text.clear()
            hideKeyboard()
        }
        onItemDraw(purchaseAdapter.getData())
    }

    private fun showSearchBar() {
        text_title_fragment_purchase.hide()
        button_search.hide()
        input_search.show()
        button_clear_search.show()
        input_search.showKeyboard()
        onItemDraw(purchaseAdapter.getData())
    }
}