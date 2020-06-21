package com.bookstore.di

import com.bookstore.database.local.LocalDatabase
import com.bookstore.database.remote.RemoteDatabase
import com.bookstore.repository.BookRepository
import com.bookstore.repository.CartRepository
import com.bookstore.repository.TransactionRepository
import com.bookstore.repository.UserRepository
import com.bookstore.ui.book.DetailBookViewModel
import com.bookstore.ui.checkout.CheckoutViewModel
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.main.fragment.book.BookViewModel
import com.bookstore.ui.main.fragment.cart.CartViewModel
import com.bookstore.ui.search.SearchBookViewModel
import com.bookstore.ui.signin.SignInViewModel
import com.bookstore.ui.splashscreen.SplashScreenViewModel
import com.bookstore.ui.wishlist.WishlistViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // DAO
    single { RemoteDatabase.userDAO }
    single { RemoteDatabase.bookDAO }
    single { RemoteDatabase.cartDAO }
    single { RemoteDatabase.transactionDAO }
    single { LocalDatabase.userDAO }

    // Repository
    single { UserRepository(get(), get()) }
    single { BookRepository(get(), get()) }
    single { CartRepository(get(), get()) }
    single { TransactionRepository(get(), get()) }

    // View Model
    viewModel { SplashScreenViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { BookViewModel(get(), get()) }
    viewModel { CartViewModel(get(), get()) }
    viewModel { DetailBookViewModel(get(), get(), get()) }
    viewModel { SearchBookViewModel(get(), get()) }
    viewModel { WishlistViewModel(get(), get()) }
    viewModel { CheckoutViewModel(get(), get(), get()) }
}