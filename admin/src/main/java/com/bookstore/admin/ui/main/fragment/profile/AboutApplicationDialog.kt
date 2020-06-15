package com.bookstore.admin.ui.main.fragment.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookstore.admin.BuildConfig
import com.bookstore.admin.R
import com.bookstore.admin.config.AppConfig
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_about_application.*

class AboutApplicationDialog private constructor() : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AboutApplicationDialog"

        fun createInstance(): AboutApplicationDialog = AboutApplicationDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_about_application, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_app_version.text =
            getString(R.string.text_about_application_version, BuildConfig.VERSION_NAME)
        button_website.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(AppConfig.CILSY_SEKOLAH_MOBILE_WEB_URL)
                )
            )
        }
    }
}