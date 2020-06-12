package com.bookstore.admin.model.formatted.user

import com.bookstore.admin.model.response.user.AccessToken
import com.bookstore.admin.model.status.SessionStatus

data class SessionResponse(
    val status: SessionStatus = SessionStatus.UNKNOWN,
    val accessToken: AccessToken? = null
)