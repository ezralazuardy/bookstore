package com.bookstore.admin.di

import com.bookstore.admin.database.local.LocalDatabase
import com.bookstore.admin.database.remote.RemoteDatabase
import com.bookstore.admin.repository.BookRepository
import com.bookstore.admin.repository.CartRepository
import com.bookstore.admin.repository.TransactionRepository
import com.bookstore.admin.repository.UserRepository
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
//    viewModel { SplashScreenViewModel(get(), get()) }
//    viewModel { SignInViewModel(get(), get()) }
//    viewModel { MainViewModel(get(), get()) }
//    viewModel { BookViewModel(get(), get()) }
//    viewModel { CartViewModel(get(), get()) }
//    viewModel { BookDetailViewModel(get(), get(), get()) }
//    viewModel { SearchBookViewModel(get(), get()) }
//    viewModel { WishlistViewModel(get(), get()) }
//    viewModel { CheckoutViewModel(get(), get(), get()) }
}