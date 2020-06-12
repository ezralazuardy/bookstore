package com.bookstore.model.request.user

import com.bookstore.config.AppConfig.OAUTH_DEFAULT_GRANT_TYPE
import com.bookstore.config.AppConfig.OAUTH_DEFAULT_SCOPE

data class AccessTokenRequest(
    val grantType: String = OAUTH_DEFAULT_GRANT_TYPE,
    val scope: String = OAUTH_DEFAULT_SCOPE,
    val username: String,
    val password: String
)