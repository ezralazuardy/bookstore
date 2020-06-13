package com.bookstore.ui.main.fragment.cart.adapter

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
import kotlinx.android.synthetic.main.item_list_cart.view.*
import java.util.*

class CartAdapter(
    private val cartItemListener: CartItemListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>(), CartFilterable {

    private val originalCarts = mutableListOf<CartDetail>()
    private var carts = listOf<CartDetail>()

    fun setData(carts: List<CartDetail>) {
        this.originalCarts.clear()
        this.originalCarts.addAll(carts)
        this.carts = originalCarts
        notifyDataSetChanged()
        cartItemListener.onItemDraw(originalCarts)
    }

    fun getData() = originalCarts

    fun removeDataAt(position: Int) {
        originalCarts.removeAt(position)
        carts = originalCarts
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, carts.size)
        cartItemListener.onItemDraw(originalCarts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart, parent, false))

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position, carts[position])

    override fun performFilterByName(bookName: String?) {
        carts = originalCarts
        if(!bookName.isNullOrEmpty()) carts = originalCarts.filter {
            it.bookModel.title.trim().toLowerCase(Locale.getDefault())
                .contains(bookName.trim().toLowerCase(Locale.getDefault()))
        }
        cartItemListener.onItemSearch(carts.isEmpty())
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("DefaultLocale")
        fun bind(position: Int, cartDetail: CartDetail) {
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
                cartItemListener.onItemClick(cartDetail)
            }
            itemView.button_remove.setOnClickListener {
                itemView.button_remove.isEnabled = false
                removeDataAt(position)
                cartItemListener.onItemRemove(position, cartDetail)
            }
        }
    }
}