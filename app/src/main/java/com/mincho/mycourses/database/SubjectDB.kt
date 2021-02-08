package com.mincho.mycourses.database

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

/**
 *Created by Mincho Simov on 07/02/2021.
 */
object SubjectDB {
    internal const val TABLE_NAME = "Subjects"

    /**
     * The URI to access the table
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"

    object Columns {
        const val ID = BaseColumns._ID
        const val NAME = "Name"
        const val YEAR = "Year"
        const val TERM = "Term"

    }

    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id:Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI,id)
    }

}