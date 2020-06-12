package com.bookstore.admin.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bookstore.admin.config.AppConfig
import com.bookstore.admin.dao.local.LocalUserDAO
import com.bookstore.admin.model.response.user.AccessToken

@Database(entities = [AccessToken::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract val userDAO: LocalUserDAO

    companion object {

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    AppConfig.ROOM_DEFAULT_DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}