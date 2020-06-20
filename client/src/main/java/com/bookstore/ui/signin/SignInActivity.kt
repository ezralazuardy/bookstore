package com.bookstore.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bookstore.R
import com.bookstore.config.AppConfig.OAUTH_DEFAULT_ACCOUNT_PASSWORD
import com.bookstore.config.AppConfig.OAUTH_DEFAULT_ACCOUNT_USERNAME
import com.bookstore.constant.RetrofitStatus
import com.bookstore.ui.main.MainActivity
import com.bookstore.utils.ViewHelper.invisible
import com.bookstore.utils.ViewHelper.show
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private val signInViewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signInViewModel.signInResponse.observe(this, Observer {
            if(it.status == RetrofitStatus.SUCCESS) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                loading.invisible()
                button_signin.isEnabled = true
                Snackbar.make(parent_layout, "There is an error when trying to sign in", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        button_signin.setOnClickListener {
            val username = input_username.editText?.text.toString()
            val password = input_password.editText?.text.toString()
            input_username.error = if (username.isEmpty()) "Please type your username" else null
            input_password.error = if (password.isEmpty()) "Please type your password" else null
            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (username == OAUTH_DEFAULT_ACCOUNT_USERNAME && password == OAUTH_DEFAULT_ACCOUNT_PASSWORD) {
                    loading.show()
                    button_signin.isEnabled = false
                    signInViewModel.signIn(username.trim(), password.trim())
                } else {
                    input_username.error =
                        if (username != OAUTH_DEFAULT_ACCOUNT_USERNAME) "We can't find your username" else null
                    input_password.error =
                        if (password != OAUTH_DEFAULT_ACCOUNT_PASSWORD) "Your password is wrong" else null
                    Snackbar.make(
                        parent_layout,
                        "Your username or password is wrong, please recheck",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
