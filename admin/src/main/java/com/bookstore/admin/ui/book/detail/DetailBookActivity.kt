package com.bookstore.admin.ui.book.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bookstore.admin.R
import com.bookstore.admin.model.response.book.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_detail_book.*

class DetailBookActivity : AppCompatActivity() {

    companion object {
        const val DATA = "book_detail_activity_data"
    }

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)
        intent.getParcelableExtra<Book>(DATA).let { book ->
            if (book != null) {
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
                        ): Boolean = false

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            image_cover.setOnClickListener {
                                StfalconImageViewer.Builder(
                                    this@DetailBookActivity,
                                    arrayOf(resource)
                                ) { view, image ->
                                    Glide.with(this@DetailBookActivity).load(image).into(view)
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
            } else throw IllegalArgumentException("Book have'nt been set on DetailBookActivity")
        }
    }
}