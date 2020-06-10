package com.bookstore.dao.remote

import com.bookstore.model.response.user.AccessToken
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RemoteUserDAO {

    @FormUrlEncoded
    @POST("/oauth/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): AccessToken
}