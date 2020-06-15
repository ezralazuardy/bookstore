package com.bookstore.admin.ui.main.fragment.book

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
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.ui.book.add.AddBookActivity
import com.bookstore.admin.ui.book.edit.EditBookActivity
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.ui.main.fragment.book.adapter.BookAdapter
import com.bookstore.admin.ui.main.fragment.book.adapter.BookItemListener
import com.bookstore.admin.utils.ViewHelper.hide
import com.bookstore.admin.utils.ViewHelper.hideKeyboard
import com.bookstore.admin.utils.ViewHelper.show
import com.bookstore.admin.utils.ViewHelper.showKeyboard
import kotlinx.android.synthetic.main.fragment_book.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class BookFragment : Fragment(), BookItemListener {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val bookViewModel: BookViewModel by viewModel()
    private val bookAdapter by lazy {
        BookAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_book, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookViewModel.bookResponse.observe(viewLifecycleOwner, Observer {
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when (it.status) {
                RetrofitStatus.SUCCESS -> it.list?.let { list ->
                    button_search.isEnabled = true
                    placeholder_empty.hide()
                    recyclerview.show()
                    bookAdapter.setData(list)
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> {
                    button_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty.show()
                    layout_book_count.hide()
                }
            }
        })
        button_search.setOnClickListener {
            showSearchBar()
        }
        button_add.setOnClickListener {
            startActivity(Intent(requireContext(), AddBookActivity::class.java))
        }
        button_clear_search.setOnClickListener {
            hideSearchBar()
            bookAdapter.performFilterByName(null)
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                bookAdapter.performFilterByName(input_search.text.toString())
                input_search.hideKeyboard()
            }
            true
        }
        swipe_refresh_layout.setOnRefreshListener {
            bookViewModel.getBook()
        }
        recyclerview.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        button_search.isEnabled = false
        hideSearchBar()
    }

    override fun onResume() {
        super.onResume()
        bookViewModel.getBook()
    }

    override fun onItemSearch(empty: Boolean) {
        if (empty && !placeholder_empty.isShown) placeholder_empty.show()
        else placeholder_empty.hide()
    }

    override fun onItemClick(book: Book) {
        startActivity(
            Intent(requireContext(), EditBookActivity::class.java).putExtra(
                EditBookActivity.DATA, book
            )
        )
    }

    override fun onItemDraw(books: List<Book>) {
        when {
            input_search.isShown -> layout_book_count.hide()
            books.isEmpty() -> layout_book_count.hide()
            else -> {
                text_book_count.text = books.count().toString()
                layout_book_count.show()
            }
        }
    }

    private fun hideSearchBar() {
        placeholder_empty.hide()
        input_search.hide()
        button_clear_search.hide()
        text_title_fragment_book.show()
        button_search.show()
        button_add.show()
        input_search.apply {
            text.clear()
            hideKeyboard()
        }
        onItemDraw(bookAdapter.getData())
    }

    private fun showSearchBar() {
        text_title_fragment_book.hide()
        button_search.hide()
        button_add.hide()
        input_search.show()
        button_clear_search.show()
        input_search.showKeyboard()
        onItemDraw(bookAdapter.getData())
    }
}