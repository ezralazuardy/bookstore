package com.bookstore.ui.checkout.fragment.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookstore.R
import com.bookstore.constant.CartStatus
import com.bookstore.model.response.cart.CartDetail
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.book.DetailBookActivity
import com.bookstore.ui.checkout.CheckoutViewModel
import com.bookstore.ui.checkout.fragment.checkout.adapter.CheckoutAdapter
import com.bookstore.ui.checkout.fragment.checkout.adapter.CheckoutItemListener
import com.bookstore.utils.ViewHelper.hide
import com.bookstore.utils.ViewHelper.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CheckoutFragment : Fragment(), CheckoutItemListener {

    private val checkoutViewModel: CheckoutViewModel by sharedViewModel()
    private val checkoutAdapter by lazy {
        CheckoutAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_checkout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkoutViewModel.cartResponse.observe(viewLifecycleOwner, Observer { cart ->
            swipe_refresh_layout.isRefreshing = false
            loading.hide()
            when(cart.status) {
                RetrofitStatus.SUCCESS -> cart.cart?.details?.let { list ->
                    button_checkout.isEnabled = true
                    placeholder_empty.hide()
                    recyclerview.show()
                    checkoutAdapter.setData(list.sortedBy { it.id }.filter { it.cartDetailStatus == CartStatus.CARTED.toString()})
                }
                RetrofitStatus.UNAUTHORIZED -> Log.e(this::class.java.simpleName, "Your session is expired, please resign-in")
                else -> {
                    recyclerview.hide()
                    placeholder_empty.show()
                }
            }
        })
        checkoutViewModel.checkoutResponse.observe(viewLifecycleOwner, Observer { checkout ->
            swipe_refresh_layout.isRefreshing = false
            when(checkout.status) {
                RetrofitStatus.SUCCESS -> Log.d(this::class.java.simpleName, "Successfully perform checkout")
                RetrofitStatus.UNAUTHORIZED -> Log.e(this::class.java.simpleName, "Your session is expired, please resign-in")
                else -> {
                    Snackbar.make(swipe_refresh_layout, "Error occurred when performing checkout", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        swipe_refresh_layout.setOnRefreshListener {
            checkoutViewModel.getCart()
        }
        recyclerview.apply {
            adapter = checkoutAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        button_checkout.setOnClickListener {
            button_checkout.isEnabled = false
            swipe_refresh_layout.isRefreshing = true
            performCheckout()
        }
        checkoutViewModel.getCart()
    }

    override fun onItemClick(cartDetail: CartDetail) {
        val intent = Intent(requireContext(), DetailBookActivity::class.java)
        startActivity(intent.putExtra(DetailBookActivity.DATA, cartDetail.bookModel))
    }

    override fun onItemDraw(cartDetails: List<CartDetail>) {
        val totalPrice = cartDetails.map { it.bookModel.price.toLong() }.sum()
        text_total_price.text = getString(R.string.text_item_cart_book_price, totalPrice)
    }

    private fun performCheckout() {
        val cartDetailIds = checkoutAdapter.getData().map { it.id }
        checkoutViewModel.performCheckout(cartDetailIds)
    }
}