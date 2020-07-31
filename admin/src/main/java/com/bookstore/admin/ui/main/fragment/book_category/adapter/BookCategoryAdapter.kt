package com.bookstore.admin.ui.main.fragment.book_category.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookstore.admin.R
import com.bookstore.admin.model.response.book.BookCategory
import kotlinx.android.synthetic.main.item_list_book_category.view.*
import java.util.*

class BookCategoryAdapter(
    private val bookCategoryItemListener: BookCategoryItemListener
) : RecyclerView.Adapter<BookCategoryAdapter.ViewHolder>(), BookCategoryFilterable {

    private val originalBookCategories = mutableListOf<BookCategory>()
    private var bookCategories = listOf<BookCategory>()

    fun setData(bookCategories: List<BookCategory>) {
        this.originalBookCategories.clear()
        this.originalBookCategories.addAll(bookCategories)
        this.bookCategories = bookCategories
        notifyDataSetChanged()
        bookCategoryItemListener.onItemDraw(originalBookCategories)
    }

    fun getData() = originalBookCategories

    fun addData(bookCategory: BookCategory) {
        originalBookCategories.add(bookCategory)
        this.bookCategories = originalBookCategories
        notifyDataSetChanged()
    }

    fun updateData(bookCategory: BookCategory) {
        val indexTarget = originalBookCategories.indexOfFirst { it.id == bookCategory.id }
        originalBookCategories[indexTarget] = bookCategory
        this.bookCategories = originalBookCategories
        notifyDataSetChanged()
    }

    fun deleteData(bookCategory: BookCategory) {
        val indexTarget = originalBookCategories.indexOfFirst { it.id == bookCategory.id }
        originalBookCategories.removeAt(indexTarget)
        this.bookCategories = originalBookCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_book_category, parent, false)
        )

    override fun getItemCount(): Int = bookCategories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(bookCategories[position])

    override fun performFilterByName(bookCategoryName: String?) {
        bookCategories = originalBookCategories
        if (!bookCategoryName.isNullOrEmpty()) bookCategories = originalBookCategories.filter {
            it.name.trim().toLowerCase(Locale.getDefault())
                .contains(bookCategoryName.trim().toLowerCase(Locale.getDefault()))
        }
        bookCategoryItemListener.onItemSearch(bookCategories.isEmpty())
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("DefaultLocale")
        fun bind(bookCategory: BookCategory) {
            itemView.text_name.text = bookCategory.name.trim().capitalize()
            itemView.card.setOnClickListener {
                bookCategoryItemListener.onItemClick(bookCategory)
            }
        }
    }
}