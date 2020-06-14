package com.bookstore.ui.book

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bookstore.R
import com.bookstore.model.response.book.Book
import com.bookstore.model.status.RetrofitStatus
import com.bookstore.ui.main.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_book_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class BookDetailActivity : AppCompatActivity() {

    companion object {
        const val DATA = "book_detail_activity_data"
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val bookDetailViewModel: BookDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        intent.getParcelableExtra<Book>(DATA).let { book ->
            if(book != null) {
                bookDetailViewModel.cartResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_add_to_cart.isEnabled = true
                    button_add_to_cart_footer.isEnabled  = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> refreshAddToCartButtonIcon()
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when getting your cart data")
                    }
                })
                bookDetailViewModel.addCartResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_add_to_cart.isEnabled = true
                    button_add_to_cart_footer.isEnabled  = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> {
                            button_add_to_cart.setImageResource(R.drawable.ic_shopping_cart_white)
                            button_add_to_cart_footer.apply {
                                icon = ContextCompat.getDrawable(this@BookDetailActivity, R.drawable.ic_shopping_cart_white)
                                text = getString(R.string.button_text_remove_from_cart)
                            }
                            showSnackbar("This book has been added to your cart")
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when adding book to your cart")
                    }
                })
                bookDetailViewModel.removeCartResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_add_to_cart.isEnabled = true
                    button_add_to_cart_footer.isEnabled  = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> {
                            button_add_to_cart.setImageResource(R.drawable.ic_add_shopping_cart_white)
                            button_add_to_cart_footer.apply {
                                icon = ContextCompat.getDrawable(this@BookDetailActivity, R.drawable.ic_add_shopping_cart_white)
                                text = getString(R.string.button_text_add_to_cart)
                            }
                            showSnackbar("This book has been removed from your cart")
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when removing book from your cart")
                    }
                })
                bookDetailViewModel.favouriteBookResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_favourite.isEnabled = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> refreshAddToFavouriteButtonIcon()
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when getting your favourite book data")
                    }
                })
                bookDetailViewModel.addFavouriteBookResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_favourite.isEnabled = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> {
                            button_favourite.setImageResource(R.drawable.ic_favorite_white)
                            showSnackbar("This book has been added to your favourite")
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when adding book to your favourite")
                    }
                })
                bookDetailViewModel.removeFavouriteBookResponse.observe(this, Observer {
                    swipe_refresh_layout.isRefreshing = false
                    button_favourite.isEnabled = true
                    when(it.status) {
                        RetrofitStatus.SUCCESS -> {
                            button_favourite.setImageResource(R.drawable.ic_favorite_border_white)
                            showSnackbar("This book has been removed from your favourite")
                        }
                        RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(this)
                        else -> showSnackbar("Error occurred when removing book from your favourite")
                    }
                })
                setContent(book)
            } else {
                Log.e(this::class.java.simpleName, "Error happened, can't receive book data")
                Toast.makeText(this, "Error happened, can't receive book data", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setContent(book: Book) {
        button_add_to_cart.isEnabled = false
        button_add_to_cart_footer.isEnabled = false
        button_favourite.isEnabled = false
        Glide.with(this)
            .load(book.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .placeholder(R.color.colorShimmer)
            .error(R.color.colorShimmer)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean  = false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    image_cover.setOnClickListener {
                        StfalconImageViewer.Builder(this@BookDetailActivity, arrayOf(resource)) { view, image ->
                            Glide.with(this@BookDetailActivity).load(image).into(view)
                        }.withTransitionFrom(image_cover).withHiddenStatusBar(false).show()
                    }
                    return false
                }
            })
            .into(image_cover)
        text_author.text = book.authorName.trim().capitalize()
        text_price.text = getString(R.string.text_book_price, book.price.toLong())
        text_title.text = book.title.trim().capitalize()
        text_title_app_bar.text = book.title.trim().capitalize()
        text_category.text = book.bookCategory.name.trim().capitalize()
        text_synopsis.text = book.synopsis.trim().capitalize()
        button_back.setOnClickListener {
            super.onBackPressed()
        }
        swipe_refresh_layout.setOnRefreshListener {
            button_add_to_cart.isEnabled = false
            button_add_to_cart_footer.isEnabled = false
            button_favourite.isEnabled = false
            bookDetailViewModel.getCart(book.id)
            bookDetailViewModel.getFavouriteBook(book.id)
        }
        button_add_to_cart.setOnClickListener {
            performAddOrRemoveCart(book)
        }
        button_add_to_cart_footer.setOnClickListener {
            performAddOrRemoveCart(book)
        }
        button_favourite.setOnClickListener {
            performAddOrRemoveFavourite(book)
        }
        bookDetailViewModel.getCart(book.id)
        bookDetailViewModel.getFavouriteBook(book.id)
    }

    private fun refreshAddToCartButtonIcon() {
        if(bookDetailViewModel.cartAdded) {
            button_add_to_cart.setImageResource(R.drawable.ic_shopping_cart_white)
            button_add_to_cart_footer.apply {
                icon = ContextCompat.getDrawable(this@BookDetailActivity, R.drawable.ic_shopping_cart_white)
                text = getString(R.string.button_text_remove_from_cart)
            }
        } else {
            button_add_to_cart.setImageResource(R.drawable.ic_add_shopping_cart_white)
            button_add_to_cart_footer.apply {
                icon = ContextCompat.getDrawable(this@BookDetailActivity, R.drawable.ic_add_shopping_cart_white)
                text = getString(R.string.button_text_add_to_cart)
            }
        }
    }

    private fun performAddOrRemoveCart(book: Book) {
        swipe_refresh_layout.isRefreshing = true
        button_add_to_cart.isEnabled = false
        button_add_to_cart_footer.isEnabled = false
        if(bookDetailViewModel.cartAdded) bookDetailViewModel.removeBookFromCart(book.id)
        else bookDetailViewModel.addBookToCart(book.id)
    }

    private fun refreshAddToFavouriteButtonIcon() {
        if(bookDetailViewModel.favouriteBookAdded) button_favourite.setImageResource(R.drawable.ic_favorite_white)
        else button_favourite.setImageResource(R.drawable.ic_favorite_border_white)
    }

    private fun performAddOrRemoveFavourite(book: Book) {
        swipe_refresh_layout.isRefreshing = true
        button_favourite.isEnabled = false
        if(bookDetailViewModel.favouriteBookAdded) bookDetailViewModel.removeBookFromFavourite(book.id)
        else bookDetailViewModel.addBookToFavourite(book.id)
    }

    private fun showSnackbar(msg: String) = Snackbar.make(parent_layout, msg, Snackbar.LENGTH_SHORT).show()
}
