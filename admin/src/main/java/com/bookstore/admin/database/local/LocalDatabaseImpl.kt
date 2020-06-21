package com.bookstore.admin.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bookstore.admin.config.AppConfig
import com.bookstore.admin.dao.local.LocalUserDAO
import com.bookstore.admin.model.response.user.AccessToken
import org.koin.core.KoinComponent
import org.koin.core.inject

@Database(entities = [AccessToken::class], version = 1, exportSchema = false)
internal abstract class LocalDatabaseImpl : RoomDatabase() {

    internal abstract val userDAO: LocalUserDAO

    companion object : KoinComponent {

        private val context: Context by inject()

        internal val database =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabaseImpl::class.java,
                AppConfig.ROOM_DEFAULT_DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
    }
}