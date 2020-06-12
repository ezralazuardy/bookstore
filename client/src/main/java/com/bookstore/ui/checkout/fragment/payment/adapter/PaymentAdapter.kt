package com.bookstore.ui.checkout.fragment.payment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.R
import com.bookstore.model.response.transaction.TransactionDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_list_payment.view.*

class PaymentAdapter(
    private val paymentItemListener: PaymentItemListener
) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    private val transactionDetails = mutableListOf<TransactionDetail>()

    fun setData(transactionDetails: List<TransactionDetail>) {
        this.transactionDetails.clear()
        this.transactionDetails.addAll(transactionDetails)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_payment, parent, false)
        )

    override fun getItemCount(): Int = transactionDetails.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(transactionDetails[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("DefaultLocale")
        fun bind(transactionDetail: TransactionDetail) {
            Glide.with(itemView.context)
                .load(transactionDetail.bookModel.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .placeholder(R.color.colorShimmer)
                .error(R.color.colorShimmer)
                .into(itemView.image_cover)
            itemView.text_name.text = transactionDetail.bookModel.title.trim().capitalize()
            itemView.card.setOnClickListener {
                paymentItemListener.onItemClick(transactionDetail)
            }
        }
    }
}