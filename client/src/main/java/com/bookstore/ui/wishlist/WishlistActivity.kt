package com.bookstore.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bookstore.R
import com.bookstore.constant.BookStatus
import com.bookstore.model.response.book.Book
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.book.BookDetailActivity
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.wishlist.adapter.WishlistAdapter
import com.bookstore.ui.wishlist.adapter.WishlistItemListener
import com.bookstore.utils.ViewHelper.hide
import com.bookstore.utils.ViewHelper.hideKeyboard
import com.bookstore.utils.ViewHelper.show
import com.bookstore.utils.ViewHelper.showKeyboard
import kotlinx.android.synthetic.main.activity_wishlist.*
import org.koin.android.viewmodel.ext.android.viewModel

class WishlistActivity : AppCompatActivity(), WishlistItemListener {

    private val mainViewModel: MainViewModel by viewModel()
    private val wishlistViewModel: WishlistViewModel by viewModel()
    private val wishListAdapter by lazy {
        WishlistAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        wishlistViewModel.favouriteResponse.observe(this, Observer { favouriteBook ->
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when(favouriteBook.status) {
                RetrofitStatus.SUCCESS -> favouriteBook.favouriteBook?.details?.map { it.bookModel }?.let { list ->
                    button_search.isEnabled = true
                    placeholder_empty_wishlist.hide()
                    recyclerview.show()
                    wishListAdapter.setData(list.sortedBy { it.id }.filter { it.bookStatus == BookStatus.FOR_SELL.toString() })
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> {
                    button_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty_wishlist.show()
                }
            }
        })
        button_search.setOnClickListener {
            showSearchBar()
        }
        button_clear_search.setOnClickListener {
            hideSearchBar()
            wishListAdapter.performFilterByName(null)
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                wishListAdapter.performFilterByName(input_search.text.toString())
                input_search.hideKeyboard()
            }
            true
        }
        swipe_refresh_layout.setOnRefreshListener {
            wishlistViewModel.getFavouriteBook()
        }
        recyclerview.apply {
            adapter = wishListAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }
        button_search.isEnabled = false
        hideSearchBar()
    }

    override fun onResume() {
        super.onResume()
        wishlistViewModel.getFavouriteBook()
    }

    override fun onItemSearch(empty: Boolean) {
        if(empty && !placeholder_empty_wishlist.isShown) placeholder_empty_search.show()
        else placeholder_empty_search.hide()
    }

    override fun onItemClick(book: Book) {
        val intent = Intent(this, BookDetailActivity::class.java)
        startActivity(intent.putExtra(BookDetailActivity.DATA, book))
    }

    override fun onBackPressed() {
        if(input_search.isShown) {
            hideSearchBar()
            wishListAdapter.performFilterByName(null)
        } else super.onBackPressed()
    }

    private fun hideSearchBar() {
        placeholder_empty_search.hide()
        input_search.hide()
        button_clear_search.hide()
        text_title_activity_wishlist.show()
        button_search.show()
        button_back.setOnClickListener {
            super.onBackPressed()
        }
        input_search.apply {
            text.clear()
            hideKeyboard()
        }
    }

    private fun showSearchBar() {
        text_title_activity_wishlist.hide()
        button_search.hide()
        input_search.show()
        button_clear_search.show()
        button_back.setOnClickListener {
            hideSearchBar()
            wishListAdapter.performFilterByName(null)
        }
        input_search.showKeyboard()
    }
}
