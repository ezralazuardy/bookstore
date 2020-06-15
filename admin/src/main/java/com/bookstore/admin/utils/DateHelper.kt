package com.bookstore.admin.utils

import com.bookstore.admin.config.AppConfig.DATE_TIME_DATABASE_FORMAT
import com.bookstore.admin.config.AppConfig.DATE_TIME_DEFAULT_FORMAT
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateHelper {

    fun formatFromString(
        stringDateTime: String,
        newFormat: String = DATE_TIME_DEFAULT_FORMAT
    ): String =
        LocalDateTime.parse(stringDateTime, DateTimeFormatter.ofPattern(DATE_TIME_DATABASE_FORMAT))
            .format(DateTimeFormatter.ofPattern(newFormat))
}