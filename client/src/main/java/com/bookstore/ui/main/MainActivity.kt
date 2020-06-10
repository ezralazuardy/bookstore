package com.bookstore.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bookstore.R
import com.bookstore.model.status.SessionStatus
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
        mainViewModel.sessionResponse.observe(this, Observer {
            if(it.status == SessionStatus.UNAVAILABLE) mainViewModel.logout(this)
        })
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.checkSession()
    }
}
