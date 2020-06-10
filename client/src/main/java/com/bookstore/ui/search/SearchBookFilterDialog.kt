package com.bookstore.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookstore.R
import com.bookstore.constant.BookType
import com.bookstore.ui.search.adapter.SearchBookItemListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_search_book_filter.*

class SearchBookFilterDialog(
    private val searchBookItemListener: SearchBookItemListener,
    private val bookTypes: List<BookType>
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "SearchBookFilterDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_search_book_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookTypes.distinct().forEach {
            if(it == BookType.FICTION) checkbox_fiction.isChecked = true
            if(it == BookType.HISTORY) checkbox_history.isChecked = true
            if(it == BookType.HORROR) checkbox_horror.isChecked = true
            if(it == BookType.NON_FICTION) checkbox_non_fiction.isChecked = true
            if(it == BookType.ROMANCE) checkbox_romance.isChecked = true
            if(it == BookType.SCIENCE) checkbox_science.isChecked = true
            if(it == BookType.ART) checkbox_art.isChecked = true
            if(it == BookType.PSYCHOLOGY) checkbox_psychology.isChecked = true
        }
        button_apply_filter.setOnClickListener {
            val filterByTypes = mutableListOf<BookType>()
            if(checkbox_fiction.isChecked) filterByTypes.add(BookType.FICTION)
            if(checkbox_history.isChecked) filterByTypes.add(BookType.HISTORY)
            if(checkbox_horror.isChecked) filterByTypes.add(BookType.HORROR)
            if(checkbox_non_fiction.isChecked) filterByTypes.add(BookType.NON_FICTION)
            if(checkbox_romance.isChecked) filterByTypes.add(BookType.ROMANCE)
            if(checkbox_science.isChecked) filterByTypes.add(BookType.SCIENCE)
            if(checkbox_art.isChecked) filterByTypes.add(BookType.ART)
            if(checkbox_psychology.isChecked) filterByTypes.add(BookType.PSYCHOLOGY)
            searchBookItemListener.onFilterByType(filterByTypes)
            this.dismiss()
        }
        button_clear_filter.setOnClickListener {
            checkbox_fiction.isChecked = false
            checkbox_history.isChecked = false
            checkbox_horror.isChecked = false
            checkbox_non_fiction.isChecked = false
            checkbox_romance.isChecked = false
            checkbox_science.isChecked = false
            checkbox_art.isChecked = false
            checkbox_psychology.isChecked = false
            searchBookItemListener.onFilterByType(mutableListOf())
            this.dismiss()
        }
    }
}