package com.bookstore.admin.ui.book.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bookstore.admin.R
import com.bookstore.admin.model.request.book.UpdateBookRequest
import com.bookstore.admin.model.response.book.Book
import com.bookstore.admin.model.status.RetrofitStatus
import com.bookstore.admin.ui.main.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.stfalcon.imageviewer.StfalconImageViewer
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_edit_book.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*

class EditBookActivity : AppCompatActivity() {

    companion object {
        const val DATA = "edit_book_activity_data"
        const val GALLERY_REQUEST_CODE = 0
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val editBookViewModel: EditBookViewModel by viewModel()

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        intent.getParcelableExtra<Book>(DATA).let { book ->
            if (book != null) {
                editBookViewModel.bookCategoryResponse.observe(this, Observer { response ->
                    swipe_refresh_layout.isRefreshing = false
                    when (response.status) {
                        RetrofitStatus.SUCCESS -> response.list?.let { list ->
                            input_text_book_category.setText(
                                book.bookCategory.name.replace(
                                    "_",
                                    " "
                                ).capitalize().trim()
                            )
                            input_text_book_category.setAdapter(
                                ArrayAdapter(
                                    this,
                                    R.layout.dropdown_menu_popup_item,
                                    list.map { it.name })
                            )
                            setBookStatus(book)
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> Snackbar.make(
                            parent_layout,
                            "There is an error when fetching book category",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
                editBookViewModel.updateBookResponse.observe(this, Observer {
                    when (it.status) {
                        RetrofitStatus.SUCCESS -> editBookViewModel.newBookCoverImage?.let { bookImage ->
                            editBookViewModel.uploadBookImage(book.id, bookImage)
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> {
                            button_edit_book_cover.isEnabled = true
                            button_save.isEnabled = true
                            button_delete.isEnabled = true
                            swipe_refresh_layout.isRefreshing = false
                            Snackbar.make(
                                parent_layout,
                                "There is an error when updating this book",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
                editBookViewModel.uploadBookImageResponse.observe(this, Observer {
                    when (it.status) {
                        RetrofitStatus.SUCCESS -> {
                            finish()
                            Toast.makeText(this, "Successfully update the book", Toast.LENGTH_SHORT)
                                .show()
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> {
                            button_edit_book_cover.isEnabled = true
                            button_save.isEnabled = true
                            button_delete.isEnabled = true
                            swipe_refresh_layout.isRefreshing = false
                            Snackbar.make(
                                parent_layout,
                                "There is an error when uploading this book image",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
                editBookViewModel.deleteBookResponse.observe(this, Observer {
                    when (it.status) {
                        RetrofitStatus.SUCCESS -> {
                            finish()
                            Toast.makeText(this, "Successfully delete book", Toast.LENGTH_SHORT)
                                .show()
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> {
                            button_edit_book_cover.isEnabled = true
                            button_save.isEnabled = true
                            button_delete.isEnabled = true
                            swipe_refresh_layout.isRefreshing = false
                            Snackbar.make(
                                parent_layout,
                                "There is an error when deleting this book",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
                editBookViewModel.changeBookCoverAction.observe(this, Observer {
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
                                        this@EditBookActivity,
                                        arrayOf(resource)
                                    ) { view, image ->
                                        Glide.with(this@EditBookActivity).load(image).into(view)
                                    }.withTransitionFrom(image_book_cover)
                                        .withHiddenStatusBar(false).show()
                                }
                                return false
                            }
                        })
                        .into(image_book_cover)
                })
                swipe_refresh_layout.setOnRefreshListener {
                    setContent(book)
                }
                button_back.setOnClickListener {
                    onBackPressed()
                }
                button_edit_book_cover.setOnClickListener {
                    performPickImage()
                }
                button_delete.setOnClickListener {
                    MaterialAlertDialogBuilder(this)
                        .setMessage(getString(R.string.dialog_delete_book))
                        .setPositiveButton(getString(R.string.button_yes)) { _, _ ->
                            button_edit_book_cover.isEnabled = false
                            button_save.isEnabled = false
                            button_delete.isEnabled = false
                            swipe_refresh_layout.isRefreshing = true
                            editBookViewModel.deleteBook(book.id)
                        }
                        .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
                        .show()
                }
                button_save.setOnClickListener {
                    val title = input_book_title.editText?.text.toString()
                    val category = editBookViewModel.currentBookCategory.firstOrNull {
                        it.name.toLowerCase() == input_text_book_category.text.toString()
                            .replace(" ", "_").toLowerCase()
                    }?.id
                    val author = input_book_author.editText?.text.toString()
                    val price = input_book_price.editText?.text.toString()
                    val synopsis = input_book_synopsis.editText?.text.toString()
                    val status = editBookViewModel.getBookStatus().firstOrNull {
                        it.toFormattedString() == input_text_book_status.text.toString()
                            .toUpperCase()
                    }.toString()
                    input_book_title.error =
                        if (title.isEmpty()) "Please input a book title" else null
                    input_book_category.error =
                        if (category == null) "Please choose a book category" else null
                    input_book_author.error =
                        if (author.isEmpty()) "Please input a book author name" else null
                    input_book_price.error =
                        if (price.isEmpty()) "Please input a book price" else null
                    input_book_synopsis.error =
                        if (synopsis.isEmpty()) "Please input a book synopsis" else null
                    input_book_status.error =
                        if (status.isEmpty()) "Please choose a book status" else null
                    if (title.isNotEmpty() && category != null && author.isNotEmpty() && price.isNotEmpty() && synopsis.isNotEmpty() && status.isNotEmpty()) {
                        button_edit_book_cover.isEnabled = false
                        button_save.isEnabled = false
                        button_delete.isEnabled = false
                        swipe_refresh_layout.isRefreshing = true
                        val updateBookRequest = UpdateBookRequest(
                            id = book.id,
                            title = title.trim().capitalize(),
                            bookCategoryId = category,
                            authorName = author.trim().capitalize(),
                            price = price.toInt(),
                            synopsis = synopsis.trim().capitalize(),
                            bookStatus = status.toUpperCase()
                        )
                        editBookViewModel.updateBook(updateBookRequest)
                    }
                }
                setContent(book)
            } else {
                Log.e(this::class.java.simpleName, "Error happened, can't receive book data")
                Toast.makeText(this, "Error happened, can't receive book data", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setContent(book: Book) {
        swipe_refresh_layout.isRefreshing = true
        button_edit_book_cover.isEnabled = false
        button_save.isEnabled = false
        button_delete.isEnabled = false
        input_book_category.isEnabled = false
        input_book_status.isEnabled = false
        editBookViewModel.getBookCategory()
        Glide.with(this)
            .downloadOnly()
            .load(book.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.color.colorShimmer)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    editBookViewModel.newBookCoverImage = resource
                    Glide.with(this@EditBookActivity)
                        .load(resource)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .transform(RoundedCorners(16))
                        .error(R.color.colorShimmer)
                        .into(image_book_cover)
                    image_book_cover.setOnClickListener {
                        StfalconImageViewer.Builder(
                            this@EditBookActivity,
                            arrayOf(resource)
                        ) { view, image ->
                            Glide.with(this@EditBookActivity).load(image).into(view)
                        }.withTransitionFrom(image_book_cover).withHiddenStatusBar(false).show()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        input_book_title.editText?.setText(book.title.trim().capitalize())
        input_book_author.editText?.setText(book.authorName.trim().capitalize())
        input_book_price.editText?.setText(book.price.toLong().toString())
        input_book_synopsis.editText?.setText(book.synopsis.trim().capitalize())
    }

    private fun setBookStatus(book: Book) {
        val bookStatus = editBookViewModel.getBookStatus()
        input_text_book_status.setText(
            book.bookStatus.replace("_", " ").toUpperCase(Locale.getDefault())
        )
        input_text_book_status.setAdapter(
            ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                bookStatus.map { it.toFormattedString() })
        )
        button_edit_book_cover.isEnabled = true
        button_save.isEnabled = true
        button_delete.isEnabled = true
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
                editBookViewModel.changeBookCover(File(it))
            }
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.dialog_cancel_update_book))
            .setPositiveButton(getString(R.string.button_yes)) { _, _ -> super.onBackPressed() }
            .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}