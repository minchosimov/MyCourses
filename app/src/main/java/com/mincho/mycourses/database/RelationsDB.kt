package com.mincho.mycourses.database

import android.content.ContentUris
import android.net.Uri

/**
 *Created by Mincho Simov on 07/02/2021.
 */
object RelationsDB {
    internal const val TABLE_NAME = "Relations"

    /**
     * The URI to access the table
     */
    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
//    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"

    object Columns {
        const val ID_STUDENT = "Students_ID"
        const val ID_SUBJECT = "Subjects_ID"
        const val ID_PROJECT = "Projects_ID"
        const val STUDENT_YEAR = "Students_year"

    }

    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id:Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI,id)
    }
}