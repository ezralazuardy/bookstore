package com.bookstore.admin.ui.main.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bookstore.admin.R
import com.bookstore.admin.constant.RetrofitStatus
import com.bookstore.admin.ui.main.MainViewModel
import com.bookstore.admin.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.bookCountResponse.observe(viewLifecycleOwner, Observer {
            homeViewModel.getBookCategoryCount()
            when (it.status) {
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> text_book_count.text = it.bookCount.toString()
            }
        })
        homeViewModel.bookCategoryCountResponse.observe(viewLifecycleOwner, Observer {
            homeViewModel.getPurchaseCount()
            when (it.status) {
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> text_book_category_count.text = it.bookCategoryCount.toString()
            }
        })
        homeViewModel.purchaseCountResponse.observe(viewLifecycleOwner, Observer {
            swipe_refresh_layout.isRefreshing = false
            when (it.status) {
                RetrofitStatus.UNAUTHORIZED -> mainViewModel.logout(requireActivity())
                else -> text_purchase_count.text = it.transactionCount.toString()
            }
        })
        swipe_refresh_layout.setOnRefreshListener {
            homeViewModel.getBookCount()
        }
        button_settings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        swipe_refresh_layout.isRefreshing = true
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getBookCount()
    }
}