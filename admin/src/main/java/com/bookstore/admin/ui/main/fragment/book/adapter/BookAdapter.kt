package com.bookstore.admin.ui.main.fragment.book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.admin.R
import com.bookstore.admin.constant.BookStatus
import com.bookstore.admin.model.response.book.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_list_book.view.*
import java.util.*

class BookAdapter(
    private val bookItemListener: BookItemListener
) : RecyclerView.Adapter<BookAdapter.ViewHolder>(), BookFilterable {

    private val originalBooks = mutableListOf<Book>()
    private var books = listOf<Book>()

    fun setData(books: List<Book>) {
        this.originalBooks.clear()
        this.originalBooks.addAll(books.filter { it.bookStatus == BookStatus.FOR_SELL.toString() })
        this.books = books
        notifyDataSetChanged()
        bookItemListener.onItemDraw(originalBooks)
    }

    fun getData() = originalBooks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_book, parent, false)
        )

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position])

    override fun performFilterByName(bookName: String?) {
        books = originalBooks
        if (!bookName.isNullOrEmpty()) books = originalBooks.filter {
            it.title.trim().toLowerCase(Locale.getDefault())
                .contains(bookName.trim().toLowerCase(Locale.getDefault()))
        }
        bookItemListener.onItemSearch(books.isEmpty())
        notifyDataSetChanged()
    }

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