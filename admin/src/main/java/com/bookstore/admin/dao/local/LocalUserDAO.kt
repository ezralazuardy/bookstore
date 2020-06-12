package com.bookstore.admin.dao.local

import androidx.room.*
import com.bookstore.admin.config.AppConfig
import com.bookstore.admin.model.response.user.AccessToken

@Dao
interface LocalUserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccessToken(accessToken: AccessToken): Long

    @Query("SELECT * FROM ${AppConfig.ROOM_DEFAULT_SESSION_TABLE_NAME} WHERE id = 0")
    suspend fun getCurrentAccessToken(): AccessToken?

    @Delete
    suspend fun removeCurrentAccessToken(accessToken: AccessToken): Int
}