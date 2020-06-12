package com.bookstore.admin.config

internal object AppConfig {

    const val CILSY_SEKOLAH_MOBILE_WEB_URL = "https://sekolahmobile.com"
    const val API_BASE_URL = "https://obstore.herokuapp.com"
    const val RETROFIT_TIMEOUT = 25L

    const val OAUTH_DEFAULT_CLIENT_ID = "obstore-client-id"
    const val OAUTH_DEFAULT_CLIENT_SECRET = "obstore-secret#do-not-tell-anyone"
    const val OAUTH_DEFAULT_GRANT_TYPE = "password"
    const val OAUTH_DEFAULT_SCOPE = "read+write"
    const val OAUTH_DEFAULT_USER_AGENT = "Android"
    const val OAUTH_DEFAULT_USER_ID = 1

    const val ROOM_DEFAULT_DATABASE_NAME = "admin_local_database"
    const val ROOM_DEFAULT_SESSION_TABLE_NAME = "admin_session_table"
}