package com.bookstore.ui.main.fragment.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookstore.R
import com.bookstore.constant.CartStatus
import com.bookstore.model.response.cart.CartDetail
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.book.BookDetailActivity
import com.bookstore.ui.checkout.CheckoutActivity
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.main.fragment.cart.adapter.CartAdapter
import com.bookstore.ui.main.fragment.cart.adapter.CartItemListener
import com.bookstore.utils.ViewHelper.hide
import com.bookstore.utils.ViewHelper.hideKeyboard
import com.bookstore.utils.ViewHelper.show
import com.bookstore.utils.ViewHelper.showKeyboard
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CartFragment : Fragment(), CartItemListener {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val cartViewModel: CartViewModel by viewModel()
    private val cartAdapter by lazy {
        CartAdapter(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.cartResponse.observe(viewLifecycleOwner, Observer { cart ->
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when(cart.status) {
                RetrofitStatus.SUCCESS -> cart.cart?.details?.let { list ->
                    button_search.isEnabled = true
                    placeholder_empty_cart.hide()
                    recyclerview.show()
                    cartAdapter.setData(list.sortedBy { it.id }.filter { it.cartDetailStatus == CartStatus.CARTED.toString()})
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> {
                    button_search.isEnabled = false
                    recyclerview.hide()
                    placeholder_empty_cart.show()
                    layout_cart_checkout.hide()
                }
            }
        })
        cartViewModel.removeCartResponse.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                RetrofitStatus.SUCCESS -> Log.d(this::class.java.simpleName, "Cart item successfully removed")
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> {
                    swipe_refresh_layout.isRefreshing = true
                    cartViewModel.getCart()
                    Snackbar.make(parent_layout, "Error occurred when removing your cart item", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        button_search.setOnClickListener {
            showSearchBar()
        }
        button_clear_search.setOnClickListener {
            hideSearchBar()
            cartAdapter.performFilterByName(null)
        }
        button_checkout_cart.setOnClickListener {
            startActivity(Intent(requireActivity(), CheckoutActivity::class.java))
        }
        input_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                cartAdapter.performFilterByName(input_search.text.toString())
                input_search.hideKeyboard()
            }
            true
        }
        swipe_refresh_layout.setOnRefreshListener {
            cartViewModel.getCart()
        }
        recyclerview.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        button_search.isEnabled = false
        hideSearchBar()
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.getCart()
    }

    override fun onItemSearch(empty: Boolean) {
        if(empty && !placeholder_empty_cart.isShown) placeholder_empty_search.show()
        else placeholder_empty_search.hide()
    }

    override fun onItemClick(cartDetail: CartDetail) {
        val intent = Intent(requireContext(), BookDetailActivity::class.java)
        startActivity(intent.putExtra(BookDetailActivity.DATA, cartDetail.bookModel))
    }

    override fun onItemRemove(position: Int, cartDetail: CartDetail) {
        cartViewModel.removeBookFromCart(cartDetail.bookModel.id)
        if(cartAdapter.getData().isEmpty()) {
            layout_cart_checkout.hide()
            button_search.isEnabled = false
            hideSearchBar()
            placeholder_empty_cart.show()
        }
    }

    override fun onItemDraw(cartDetails: List<CartDetail>) {
        when {
            input_search.isShown -> layout_cart_checkout.hide()
            cartDetails.isEmpty() -> layout_cart_checkout.hide()
            else -> {
                val totalPrice = cartDetails.map { it.bookModel.price.toLong() }.sum()
                text_total_price.text = getString(R.string.text_item_cart_book_price, totalPrice)
                layout_cart_checkout.show()
            }
        }
    }

    private fun hideSearchBar() {
        placeholder_empty_search.hide()
        input_search.hide()
        button_clear_search.hide()
        text_title_fragment_cart.show()
        button_search.show()
        input_search.apply {
            text.clear()
            hideKeyboard()
        }
        onItemDraw(cartAdapter.getData())
    }

    private fun showSearchBar() {
        text_title_fragment_cart.hide()
        button_search.hide()
        input_search.show()
        button_clear_search.show()
        input_search.showKeyboard()
        onItemDraw(cartAdapter.getData())
    }
}
