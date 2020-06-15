package com.bookstore.ui.checkout.fragment.payment

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
import com.bookstore.model.response.transaction.Transaction
import com.bookstore.model.response.transaction.TransactionDetail
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.book.DetailBookActivity
import com.bookstore.ui.checkout.CheckoutViewModel
import com.bookstore.ui.checkout.fragment.payment.adapter.PaymentAdapter
import com.bookstore.ui.checkout.fragment.payment.adapter.PaymentItemListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_payment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PaymentFragment : Fragment(), PaymentItemListener {

    private val checkoutViewModel: CheckoutViewModel by sharedViewModel()
    private lateinit var transaction: Transaction
    private val paymentAdapter by lazy {
        PaymentAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkoutViewModel.paymentResponse.observe(viewLifecycleOwner, Observer { payment ->
            when (payment.status) {
                RetrofitStatus.SUCCESS -> Log.d(
                    this::class.java.simpleName,
                    "Successfully perform payment"
                )
                RetrofitStatus.UNAUTHORIZED -> Log.e(
                    this::class.java.simpleName,
                    "Your session is expired, please resign-in"
                )
                else -> {
                    button_pay.isEnabled = true
                    Snackbar.make(
                        parent_layout,
                        "Error occurred when performing payment",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
        recyclerview.apply {
            adapter = paymentAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::transaction.isInitialized) {
            text_invoice_number.text = transaction.invoiceNumber
            text_invoice_date.text = transaction.createdTime
            text_total_price.text = getString(
                R.string.text_item_cart_book_price,
                transaction.details.map { it.price.toLong() }.sum()
            )
            button_pay.setOnClickListener {
                button_pay.isEnabled = false
                performPayment()
            }
            paymentAdapter.setData(transaction.details)
        }
    }

    override fun onItemClick(transactionDetail: TransactionDetail) {
        val intent = Intent(requireContext(), DetailBookActivity::class.java)
        startActivity(intent.putExtra(DetailBookActivity.DATA, transactionDetail.bookModel))
    }

    private fun performPayment() = checkoutViewModel.performPayment(transaction.id)

    fun setData(transaction: Transaction) {
        this.transaction = transaction
    }
}