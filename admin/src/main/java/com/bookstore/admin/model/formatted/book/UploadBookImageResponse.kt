package com.bookstore.admin.model.formatted.book

import com.bookstore.admin.model.status.RetrofitStatus

data class UploadBookImageResponse(
    val status: RetrofitStatus = RetrofitStatus.UNKNOWN
)