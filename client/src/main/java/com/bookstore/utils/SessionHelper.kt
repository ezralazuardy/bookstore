package com.bookstore.utils

import com.bookstore.model.response.user.AccessToken
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

object SessionHelper {

    val unauthorizedException by lazy {
        val response: Response<AccessToken> = Response.error(401, ResponseBody.create(MediaType.get("text"), "null"))
        HttpException(response)
    }

    fun AccessToken.asBearer(): String = "Bearer ${this.accessToken}"
}