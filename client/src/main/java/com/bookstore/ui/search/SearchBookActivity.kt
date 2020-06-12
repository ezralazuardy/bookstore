package com.bookstore.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bookstore.R
import com.bookstore.constant.BookType
import com.bookstore.model.response.book.Book
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.book.BookDetailActivity
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.search.adapter.SearchBookAdapter
import com.bookstore.ui.search.adapter.SearchBookItemListener
import com.bookstore.utils.ViewHelper.hide
import com.bookstore.utils.ViewHelper.hideKeyboard
import com.bookstore.utils.ViewHelper.show
import com.bookstore.utils.ViewHelper.showKeyboard
import kotlinx.android.synthetic.main.activity_search_book.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchBookActivity : AppCompatActivity(), SearchBookItemListener {

    companion object {
        const val FILTER_BY_TYPE = "search_book_activity_filter_by_type"
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val searchBookViewModel: SearchBookViewModel by viewModel()
    private val searchBookAdapter by lazy {
        SearchBookAdapter(this)
    }
    private val filterByTypes = mutableListOf<BookType>()
    private var filterByTypeFromIntent = false

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_book)
        searchBookViewModel.searchBookResponse.observe(this, Observer {
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when(it.status) {
                RetrofitStatus.SUCCESS -> it.list?.let { list ->
                    input_search.isEnabled = true
                    placeholder_empty.hide()
                    recyclerview.show()
                    searchBookAdapter.setData(list)
                    if(!filterByTypeFromIntent) (intent.getParcelableArrayListExtra<BookType>(FILTER_BY_TYPE))?.let { filter ->
                        onFilterByType(filter.toList())
                        filterByTypeFromIntent = true
                    }
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> {
                    input_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty.show()
                }
            }
        })
        button_back.setOnClickListener {
            super.onBackPressed()
        }
        button_filter.setOnClickListener {
            showFilterDialog()
        }
        search_bar.setOnClickListener {
            input_search.showKeyboard()
        }
        input_search.addTextChangedListener {
            if(it.toString().isNotEmpty()) button_clear_search.show()
            else button_clear_search.hide()
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBookAdapter.performFilterByName(input_search.text.toString(), filterByTypes)
                input_search.hideKeyboard()
            }
            true
        }
        button_clear_search.setOnClickListener {
            searchBookAdapter.performFilterByName(null, filterByTypes)
            input_search.apply {
                text.clear()
                hideKeyboard()
            }
        }
        card_filter_info.setOnClickListener {
            showFilterDialog()
        }
        swipe_refresh_layout.setOnRefreshListener {
            searchBookViewModel.getBook()
        }
        recyclerview.apply {
            adapter = searchBookAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
        input_search.isEnabled = false
        searchBookViewModel.getBook()
    }

    override fun onItemSearch(empty: Boolean) {
        if(empty) placeholder_empty.show() else placeholder_empty.hide()
    }

    override fun onItemClick(book: Book) {
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent.putExtra(BookDetailActivity.DATA, book))
    }

    override fun onFilterByType(bookTypes: List<BookType>) {
        filterByTypes.clear()
        filterByTypes.addAll(bookTypes)
        input_search.text.toString().let {
            if(it.isEmpty()) searchBookAdapter.performFilterByType(bookTypes)
            else searchBookAdapter.performFilterByName(it, bookTypes)
        }
        showOrHideFilterInfo()
    }

    private fun showOrHideFilterInfo() {
        if(filterByTypes.size == 0) card_filter_info.hide()
        else {
            text_card_filter_info.text = getString(R.string.text_card_filter_info, filterByTypes.size)
            card_filter_info.show()
        }
    }

    private fun showFilterDialog() =
        SearchBookFilterDialog(this, filterByTypes).show(supportFragmentManager, SearchBookFilterDialog.TAG)
}
