package com.bookstore.admin.ui.book_category.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bookstore.admin.R
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.request.book.UpdateBookCategoryRequest
import com.bookstore.admin.model.response.book.BookCategory
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.ui.main.fragment.book_category.adapter.BookCategoryItemListener
import com.bookstore.admin.utils.ViewHelper.hide
import com.bookstore.admin.utils.ViewHelper.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_edit_book_category.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EditBookCategoryDialog private constructor() : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditBookCategoryDialog"

        fun createInstance(
            bookCategory: BookCategory,
            bookCategoryItemListener: BookCategoryItemListener? = null
        ): EditBookCategoryDialog = EditBookCategoryDialog().apply {
            this.bookCategory = bookCategory
            this.bookCategoryItemListener = bookCategoryItemListener
        }
    }

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val editBookCategoryViewModel: EditBookCategoryViewModel by viewModel()
    private lateinit var bookCategory: BookCategory
    private var bookCategoryItemListener: BookCategoryItemListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_edit_book_category, container, false)

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this::bookCategory.isInitialized) {
            editBookCategoryViewModel.updateBookCategoryResponse.observe(
                viewLifecycleOwner,
                Observer { response ->
                    hideLoading()
                    when (response.status) {
                        RetrofitStatus.SUCCESS -> {
                            response.bookCategory?.let {
                                bookCategoryItemListener?.onItemUpdate(it)
                            }
                            this.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "Successfully update book category",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                        else -> Toast.makeText(
                            requireContext(),
                            "There is an error when updating book category",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            editBookCategoryViewModel.deleteBookCategoryResponse.observe(
                viewLifecycleOwner,
                Observer { response ->
                    when (response.status) {
                        RetrofitStatus.SUCCESS -> {
                            response.bookCategory?.let {
                                bookCategoryItemListener?.onItemDelete(it)
                            }
                            this.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "Successfully delete book category",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                        RetrofitStatus.CONSTRAINT_DETECTED -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                "Can't delete this book category since it's already applied on Book",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                "There is an error when deleting book category",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            input_book_category_name.editText?.setText(bookCategory.name)
            input_book_category_code.editText?.setText(bookCategory.code)
            button_cancel.setOnClickListener {
                this.dismiss()
            }
            button_save.setOnClickListener {
                val name = input_book_category_name.editText?.text.toString().trim().capitalize()
                val code = input_book_category_code.editText?.text.toString().trim().toUpperCase()
                input_book_category_name.error =
                    if (name.isEmpty()) "Please input a book category name" else null
                input_book_category_code.error =
                    if (code.isEmpty()) "Please input a book category code" else null
                if (name.isNotEmpty() && code.isNotEmpty()) {
                    showLoading()
                    val updateBookCategoryRequest = UpdateBookCategoryRequest(
                        id = bookCategory.id,
                        name = name,
                        code = code
                    )
                    editBookCategoryViewModel.updateBookCategory(updateBookCategoryRequest)
                }
            }
            button_delete.setOnClickListener {
                showLoading()
                editBookCategoryViewModel.deleteBookCategory(bookCategory.id)
            }
        } else throw IllegalArgumentException("BookCategory have'nt been set on EditBookCategoryDialog")
    }

    private fun showLoading() {
        button_delete.isEnabled = false
        button_cancel.isEnabled = false
        button_save.isEnabled = false
        loading.show()
    }

    private fun hideLoading() {
        loading.hide()
        button_delete.isEnabled = true
        button_cancel.isEnabled = true
        button_save.isEnabled = true
    }
}