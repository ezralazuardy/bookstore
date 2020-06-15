package com.bookstore.admin.ui.book.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bookstore.admin.R
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.model.request.book.AddBookRequest
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.utils.ViewHelper.hide
import com.bookstore.admin.utils.ViewHelper.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.stfalcon.imageviewer.StfalconImageViewer
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_add_book.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class AddBookActivity : AppCompatActivity() {

    companion object {
        const val GALLERY_REQUEST_CODE = 0
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val addBookViewModel: AddBookViewModel by viewModel()

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)
        addBookViewModel.bookCategoryResponse.observe(this, Observer { response ->
            swipe_refresh_layout.isRefreshing = false
            when (response.status) {
                RetrofitStatus.SUCCESS -> response.list?.let { list ->
                    input_text_book_category.setAdapter(
                        ArrayAdapter(
                            this,
                            R.layout.dropdown_menu_popup_item,
                            list.map { it.name })
                    )
                    setBookStatus()
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> Snackbar.make(
                    parent_layout,
                    "There is an error when fetching book category",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        addBookViewModel.addBookResponse.observe(this, Observer {
            when (it.status) {
                RetrofitStatus.SUCCESS -> it.addedBook?.let { addedBook ->
                    addBookViewModel.newBookCoverImage?.let { bookImage ->
                        addBookViewModel.uploadBookImage(addedBook.id, bookImage)
                    }
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> {
                    button_edit_book_cover.isEnabled = true
                    button_save.isEnabled = true
                    swipe_refresh_layout.isRefreshing = false
                    Snackbar.make(
                        parent_layout,
                        "There is an error when adding a new book",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
        addBookViewModel.uploadBookImageResponse.observe(this, Observer {
            button_edit_book_cover.isEnabled = true
            button_save.isEnabled = true
            swipe_refresh_layout.isRefreshing = false
            when (it.status) {
                RetrofitStatus.SUCCESS -> {
                    finish()
                    Toast.makeText(this, "Successfully add a new book", Toast.LENGTH_SHORT).show()
                }
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                else -> {
                    Snackbar.make(
                        parent_layout,
                        "There is an error when uploading a book image",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
        addBookViewModel.changeBookCoverAction.observe(this, Observer {
            Glide.with(this)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .transform(RoundedCorners(16))
                .error(R.color.colorShimmer)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean = false

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        image_book_cover.setOnClickListener {
                            StfalconImageViewer.Builder(
                                this@AddBookActivity,
                                arrayOf(resource)
                            ) { view, image ->
                                Glide.with(this@AddBookActivity).load(image).into(view)
                            }.withTransitionFrom(image_book_cover).withHiddenStatusBar(false).show()
                        }
                        return false
                    }
                })
                .into(image_book_cover)
        })
        swipe_refresh_layout.setOnRefreshListener {
            refreshBookCategory()
        }
        button_back.setOnClickListener {
            onBackPressed()
        }
        button_edit_book_cover.setOnClickListener {
            performPickImage()
        }
        button_save.setOnClickListener {
            val title = input_book_title.editText?.text.toString()
            val category = addBookViewModel.currentBookCategory.firstOrNull {
                it.name.toLowerCase() == input_text_book_category.text.toString().replace(" ", "_")
                    .toLowerCase()
            }?.id
            val author = input_book_author.editText?.text.toString()
            val price = input_book_price.editText?.text.toString()
            val synopsis = input_book_synopsis.editText?.text.toString()
            val status = addBookViewModel.getBookStatus().firstOrNull {
                it.toFormattedString() == input_text_book_status.text.toString().toUpperCase()
            }.toString()
            if (addBookViewModel.newBookCoverImage == null) error_book_cover.show() else error_book_cover.hide()
            input_book_title.error = if (title.isEmpty()) "Please input a book title" else null
            input_book_category.error =
                if (category == null) "Please choose a book category" else null
            input_book_author.error =
                if (author.isEmpty()) "Please input a book author name" else null
            input_book_price.error = if (price.isEmpty()) "Please input a book price" else null
            input_book_synopsis.error =
                if (synopsis.isEmpty()) "Please input a book synopsis" else null
            input_book_status.error = if (status.isEmpty()) "Please choose a book status" else null
            if (addBookViewModel.newBookCoverImage != null && title.isNotEmpty() && category != null && author.isNotEmpty() && price.isNotEmpty() && synopsis.isNotEmpty() && status.isNotEmpty()) {
                button_edit_book_cover.isEnabled = false
                button_save.isEnabled = false
                swipe_refresh_layout.isRefreshing = true
                val addBookRequest = AddBookRequest(
                    title = title.trim().capitalize(),
                    bookCategoryId = category,
                    authorName = author.trim().capitalize(),
                    price = price.toInt(),
                    synopsis = synopsis.trim().capitalize(),
                    bookStatus = status.toUpperCase()
                )
                addBookViewModel.addBook(addBookRequest)
            }
        }
        refreshBookCategory()
    }

    private fun refreshBookCategory() {
        swipe_refresh_layout.isRefreshing = true
        button_edit_book_cover.isEnabled = false
        button_save.isEnabled = false
        input_book_category.isEnabled = false
        input_book_status.isEnabled = false
        addBookViewModel.getBookCategory()
    }

    private fun setBookStatus() {
        val bookStatus = addBookViewModel.getBookStatus()
        input_text_book_status.setAdapter(
            ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                bookStatus.map { it.toFormattedString() })
        )
        button_edit_book_cover.isEnabled = true
        button_save.isEnabled = true
        input_book_category.isEnabled = true
        input_book_status.isEnabled = true
    }

    private fun performPickImage() = runWithPermissions(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .showSingleMediaType(true)
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.AppTheme_Gallery)
            .forResult(GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imagePath = Uri.parse(Matisse.obtainPathResult(data).first()).path
            imagePath?.let {
                addBookViewModel.changeBookCover(File(it))
            }
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.dialog_cancel_add_book))
            .setPositiveButton(getString(R.string.button_yes)) { _, _ -> super.onBackPressed() }
            .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}