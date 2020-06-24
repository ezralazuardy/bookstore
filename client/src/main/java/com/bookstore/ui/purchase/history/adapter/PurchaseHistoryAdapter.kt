package com.bookstore.ui.purchase.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.R
import com.bookstore.model.response.transaction.Transaction
import kotlinx.android.synthetic.main.item_list_purchase.view.*
import java.util.*

class PurchaseHistoryAdapter(
    private val purchaseHistoryItemListener: PurchaseHistoryItemListener
) : RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder>(), PurchaseHistoryFilterable {

    private val originalPurchases = mutableListOf<Transaction>()
    private var purchases = listOf<Transaction>()

    fun setData(purchases: List<Transaction>) {
        this.originalPurchases.clear()
        this.originalPurchases.addAll(purchases)
        this.purchases = purchases
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_purchase, parent, false)
        )

    override fun getItemCount(): Int = purchases.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(purchases[position])

    override fun performFilterByName(invoiceNumber: String?) {
        purchases = originalPurchases
        if (!invoiceNumber.isNullOrEmpty()) purchases = originalPurchases.filter {
            it.invoiceNumber.trim().toLowerCase(Locale.getDefault())
                .contains(invoiceNumber.trim().toLowerCase(Locale.getDefault()))
        }
        purchaseHistoryItemListener.onItemSearch(purchases.isEmpty())
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(transaction: Transaction) {
            itemView.text_number.text = transaction.invoiceNumber.trim()
            itemView.text_date.text = transaction.createdTime.trim()
            itemView.card.setOnClickListener {
                purchaseHistoryItemListener.onItemClick(transaction)
            }
        }
    }
}