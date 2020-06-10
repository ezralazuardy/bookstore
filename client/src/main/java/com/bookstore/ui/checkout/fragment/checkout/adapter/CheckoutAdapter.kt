package com.bookstore.ui.checkout.fragment.checkout.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.R
import com.bookstore.model.response.cart.CartDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_list_checkout.view.*

class CheckoutAdapter(
    private val checkoutItemListener: CheckoutItemListener
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    private val carts = mutableListOf<CartDetail>()

    fun setData(carts: List<CartDetail>) {
        this.carts.clear()
        this.carts.addAll(carts)
        notifyDataSetChanged()
        checkoutItemListener.onItemDraw(carts)
    }

    fun getData() = carts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_checkout, parent, false))

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(carts[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("DefaultLocale")
        fun bind(cartDetail: CartDetail) {
            Glide.with(itemView.context)
                .load(cartDetail.bookModel.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .placeholder(R.color.colorShimmer)
                .error(R.color.colorShimmer)
                .into(itemView.image_cover)
            itemView.text_name.text = cartDetail.bookModel.title.trim().capitalize()
            itemView.text_price.text = itemView.context.getString(R.string.text_item_cart_book_price, cartDetail.bookModel.price.toLong())
            itemView.card.setOnClickListener {
                checkoutItemListener.onItemClick(cartDetail)
            }
        }
    }
}