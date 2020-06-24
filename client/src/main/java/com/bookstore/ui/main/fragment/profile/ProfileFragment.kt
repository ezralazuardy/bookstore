package com.bookstore.ui.main.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookstore.R
import com.bookstore.ui.main.MainViewModel
import com.bookstore.ui.purchase.history.PurchaseHistoryActivity
import com.bookstore.ui.settings.SettingsActivity
import com.bookstore.ui.wishlist.WishlistActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProfileFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_logout.setOnClickListener {
            mainViewModel.logout(requireActivity())
        }
        row_menu_wishlist.setOnClickListener {
            startActivity(Intent(requireContext(), WishlistActivity::class.java))
        }
        row_menu_purchase_history.setOnClickListener {
            startActivity(Intent(requireContext(), PurchaseHistoryActivity::class.java))
        }
        row_menu_settings.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        row_menu_about.setOnClickListener {
            AboutApplicationDialog().show(
                requireActivity().supportFragmentManager,
                AboutApplicationDialog.TAG
            )
        }
    }
}
