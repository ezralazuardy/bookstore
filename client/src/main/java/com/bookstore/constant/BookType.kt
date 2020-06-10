package com.bookstore.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class BookType(val id: Int, val code: String) : Parcelable {
    FICTION(8, "BK-001"),
    HISTORY(9, "BK-002"),
    HORROR(10, "BK-003"),
    NON_FICTION(11, "BK-004"),
    ROMANCE(12, "BK-005"),
    SCIENCE(13, "BK-006"),
    ART(14, "BK-007"),
    PSYCHOLOGY(15, "BK-008")
}