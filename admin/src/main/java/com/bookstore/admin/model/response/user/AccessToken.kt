package com.bookstore.admin.model.response.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bookstore.admin.config.AppConfig
import com.google.gson.annotations.SerializedName

@Entity(tableName = AppConfig.ROOM_DEFAULT_SESSION_TABLE_NAME)
data class AccessToken(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "access_token") @SerializedName("access_token") val accessToken: String,
    @ColumnInfo(name = "expires_in") @SerializedName("expires_in") val expiresIn: Int,
    @ColumnInfo(name = "jti") @SerializedName("jti") val jti: String,
    @ColumnInfo(name = "refresh_token") @SerializedName("refresh_token") val refreshToken: String,
    @ColumnInfo(name = "scope") @SerializedName("scope") val scope: String,
    @ColumnInfo(name = "token_type") @SerializedName("token_type") val tokenType: String
)