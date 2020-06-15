package com.bookstore.admin.ui.book_category.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bookstore.admin.R
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.request.book.AddBookCategoryRequest
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.utils.ViewHelper.hide
import com.bookstore.admin.utils.ViewHelper.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_add_book_category.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AddBookCategoryDialog private constructor() : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddBookCategoryDialog"

        fun createInstance(): AddBookCategoryDialog = AddBookCategoryDialog()
    }

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val addBookCategoryViewModel: AddBookCategoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_add_book_category, container, false)

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBookCategoryViewModel.addBookCategoryResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response.status) {
                    RetrofitStatus.SUCCESS -> {
                        this.dismiss()
                        Toast.makeText(
                            requireContext(),
                            "Successfully add new book category",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                    else -> {
                        hideLoading()
                        Snackbar.make(
                            parent_layout,
                            "There is an error when adding new book category",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            })
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
                val addBookCategoryRequest = AddBookCategoryRequest(
                    name = name,
                    code = code
                )
                addBookCategoryViewModel.addBookCategory(addBookCategoryRequest)
            }
        }
    }

    private fun showLoading() {
        button_cancel.isEnabled = false
        button_save.isEnabled = false
        loading.show()
    }

    private fun hideLoading() {
        loading.hide()
        button_cancel.isEnabled = true
        button_save.isEnabled = true
    }
}