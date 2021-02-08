package com.mincho.mycourses.database

import android.content.ContentUris
import android.net.Uri

/**
 *Created by Mincho Simov on 04/02/2021.
 */
object StudentsDB {
    internal const val TABLE_NAME = "Students"

    /**
     * The URI to access the table
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"

    object Columns {
        const val F_NUMBER = "Faculty_number"
        const val F_NAME = "First_name"
        const val S_NAME = "Second_name"
        const val COURSE = "Course"
    }

    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id:Long):Uri{
        return ContentUris.withAppendedId(CONTENT_URI,id)
    }
}