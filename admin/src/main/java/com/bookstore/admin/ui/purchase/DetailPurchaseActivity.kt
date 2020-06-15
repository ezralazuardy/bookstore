package com.bookstore.admin.ui.purchase

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookstore.admin.R
import com.bookstore.admin.model.response.transaction.Transaction
import com.bookstore.admin.model.response.transaction.TransactionDetail
import com.bookstore.admin.ui.book.detail.DetailBookActivity
import com.bookstore.admin.ui.purchase.adapter.PurchaseItemListAdapter
import com.bookstore.admin.ui.purchase.adapter.PurchaseItemListListener
import com.bookstore.admin.utils.DateHelper
import kotlinx.android.synthetic.main.activity_detail_purchase.*

class DetailPurchaseActivity : AppCompatActivity(), PurchaseItemListListener {

    companion object {
        const val DATA = "purchase_detail_activity_data"
    }

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_purchase)
        intent.getParcelableExtra<Transaction>(DATA).let { transaction ->
            if (transaction != null) {
                text_invoice_number.text = transaction.invoiceNumber.trim()
                text_transaction_date.text =
                    DateHelper.formatFromString(transaction.createdTime.trim())
                text_buyer_name.text = transaction.userModel.fullName.trim().capitalize()
                text_buyer_email.text = transaction.userModel.email.trim()
                recyclerview.apply {
                    adapter =
                        PurchaseItemListAdapter(transaction.details, this@DetailPurchaseActivity)
                    layoutManager = LinearLayoutManager(this@DetailPurchaseActivity)
                    setHasFixedSize(true)
                }
                button_back.setOnClickListener {
                    super.onBackPressed()
                }
                button_copy.setOnClickListener {
                    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).run {
                        val clip =
                            ClipData.newPlainText("Invoice Number", transaction.invoiceNumber)
                        setPrimaryClip(clip)
                    }
                    Toast.makeText(this, "Invoice number copied to clipboard", Toast.LENGTH_SHORT)
                        .show()
                }
            } else throw IllegalArgumentException("Transaction have'nt been set on PurchaseDetailActivity")
        }
    }

    override fun onItemClick(transactionDetail: TransactionDetail) {
        startActivity(
            Intent(this, DetailBookActivity::class.java).putExtra(
                DetailBookActivity.DATA,
                transactionDetail.bookModel
            )
        )
    }
}