package com.bookstore.ui.main.fragment.book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.R
import com.bookstore.constant.BookStatus
import com.bookstore.model.response.book.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_list_book.view.*

class BookAdapter(
    private val bookItemListener: BookItemListener
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val books = mutableListOf<Book>()

    fun setData(books: List<Book>) {
        this.books.clear()
        this.books.addAll(books.filter { it.bookStatus == BookStatus.FOR_SELL.toString() })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_book, parent, false))

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("DefaultLocale")
        fun bind(book: Book) {
            Glide.with(itemView.context)
                .load(book.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .placeholder(R.color.colorShimmer)
                .error(R.color.colorShimmer)
                .into(itemView.image_cover)
            itemView.text_name.text = book.title.trim().capitalize()
            itemView.text_category.text = book.bookCategory.name.trim().capitalize()
            itemView.card.setOnClickListener {
                bookItemListener.onItemClick(book)
            }
        }
    }
}