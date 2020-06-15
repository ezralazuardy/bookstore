package com.bookstore.admin.model.formatted.user

import com.bookstore.admin.constant.SessionStatus
import com.bookstore.admin.model.response.user.AccessToken

data class SessionResponse(
    val status: SessionStatus = SessionStatus.UNKNOWN,
    val accessToken: AccessToken? = null
)