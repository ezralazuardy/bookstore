package com.bookstore.model.formatted.user

import com.bookstore.constant.SessionStatus
import com.bookstore.model.response.user.AccessToken

data class SessionResponse(
    val status: SessionStatus = SessionStatus.UNKNOWN,
    val accessToken: AccessToken? = null
)