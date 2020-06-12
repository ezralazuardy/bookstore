package com.bookstore.ui.checkout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.bookstore.R
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.checkout.fragment.checkout.CheckoutFragment
import com.bookstore.ui.checkout.fragment.payment.PaymentFragment
import com.bookstore.ui.checkout.fragment.payment_success.PaymentSuccessFragment
import com.bookstore.ui.main.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_checkout.*
import org.koin.android.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val checkoutViewModel: CheckoutViewModel by viewModel()
    private val checkoutFragment by lazy {
        CheckoutFragment()
    }
    private val paymentFragment by lazy {
        PaymentFragment()
    }
    private val paymentSuccessFragment by lazy {
        PaymentSuccessFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        checkoutViewModel.cartResponse.observe(this, Observer { cart ->
            when (cart.status) {
                RetrofitStatus.SUCCESS -> Log.d(
                    this::class.java.simpleName,
                    "Successfully fetch cart data"
                )
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> Log.e(this::class.java.simpleName, "Error occurred when fetching cart data")
            }
        })
        checkoutViewModel.checkoutResponse.observe(this, Observer { checkout ->
            when(checkout.status) {
                RetrofitStatus.SUCCESS -> checkout.transaction?.let { checkoutTransaction ->
                    paymentFragment.setData(checkoutTransaction)
                    supportFragmentManager.commit {
                        setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        replace(fragment_container.id, paymentFragment)
                    }
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> Log.e(this::class.java.simpleName, "Error occurred when performing checkout")
            }
        })
        checkoutViewModel.paymentResponse.observe(this, Observer { payment ->
            when(payment.status) {
                RetrofitStatus.SUCCESS -> supportFragmentManager.commit {
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    replace(fragment_container.id, paymentSuccessFragment)
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> Log.e(this::class.java.simpleName, "Error occurred when performing payment")
            }
        })
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            replace(fragment_container.id, checkoutFragment)
        }
        button_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.dialog_cancel_payment))
            .setPositiveButton(getString(R.string.button_yes)) { _, _ -> super.onBackPressed() }
            .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
