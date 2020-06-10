package com.bookstore.model.formatted.user

import com.bookstore.model.response.user.AccessToken
import com.bookstore.model.status.SessionStatus

data class SessionResponse(
    val status: SessionStatus = SessionStatus.UNKNOWN,
    val accessToken: AccessToken? = null
)