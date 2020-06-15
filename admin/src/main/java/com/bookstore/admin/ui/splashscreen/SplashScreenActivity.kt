package com.bookstore.admin.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bookstore.admin.constant.SessionStatus
import com.bookstore.admin.ui.main.MainActivity
import com.bookstore.admin.ui.signin.SignInActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenViewModel.sessionResponse.observe(this, Observer {
            when (it.status) {
                SessionStatus.AVAILABLE -> startActivity(Intent(this, MainActivity::class.java))
                else -> startActivity(Intent(this, SignInActivity::class.java))
            }
            finish()
        })
    }

    override fun onStart() {
        super.onStart()
        splashScreenViewModel.checkSession()
    }
}