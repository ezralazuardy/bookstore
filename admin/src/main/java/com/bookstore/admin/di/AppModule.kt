package com.bookstore.admin.di

import com.bookstore.admin.database.local.LocalDatabase
import com.bookstore.admin.database.remote.RemoteDatabase
import com.bookstore.admin.repository.BookRepository
import com.bookstore.admin.repository.CartRepository
import com.bookstore.admin.repository.TransactionRepository
import com.bookstore.admin.repository.UserRepository
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.ui.main.fragment.book.BookViewModel
import com.bookstore.admin.ui.main.fragment.book_category.BookCategoryViewModel
import com.bookstore.admin.ui.main.fragment.home.HomeViewModel
import com.bookstore.admin.ui.main.fragment.purchase.PurchaseViewModel
import com.bookstore.admin.ui.signin.SignInViewModel
import com.bookstore.admin.ui.splashscreen.SplashScreenViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // DAO
    single { RemoteDatabase.userDAO }
    single { RemoteDatabase.bookDAO }
    single { RemoteDatabase.cartDAO }
    single { RemoteDatabase.transactionDAO }
    single { LocalDatabase.getDatabase(get()).userDAO }

    // Repository
    single { UserRepository(get(), get()) }
    single { BookRepository(get(), get()) }
    single { CartRepository(get(), get()) }
    single { TransactionRepository(get(), get()) }

    // View Model
    viewModel { SplashScreenViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { BookViewModel(get(), get()) }
    viewModel { BookCategoryViewModel(get(), get()) }
    viewModel { PurchaseViewModel(get(), get()) }
}