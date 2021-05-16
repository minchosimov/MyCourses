package com.mincho.mycourses.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 *Created by Mincho Simov on 04/02/2021.
 * This is the ContentProvider child class
 * This is the only class that know about [AppDatabase]
 * Съобразявайки се с MVVM архитектурата сиздаваме този клас.
 * Това е единствения клас, който комуникира с базата данни
 * Достапа до мрежата става чрез URI
 */



private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "com.mincho.mycourses.provider"
val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

const val STUDENTS = 100
const val STUDENTS_ID = 101

const val SUBJECT = 200
const val SUBJECT_ID = 201

const val PROJECT = 300
const val  PROJECT_ID = 301

const val RELATIONS = 400


class AppProvider: ContentProvider(){

    private val uriMatcher by lazy { buildUriMatcher() }


    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        matcher.addURI(CONTENT_AUTHORITY, StudentsDB.TABLE_NAME, STUDENTS)
        matcher.addURI(CONTENT_AUTHORITY, "${StudentsDB.TABLE_NAME}/#", STUDENTS_ID)

        matcher.addURI(CONTENT_AUTHORITY, SubjectDB.TABLE_NAME, SUBJECT)
        matcher.addURI(CONTENT_AUTHORITY, "${SubjectDB.TABLE_NAME}/#", SUBJECT_ID)

        matcher.addURI(CONTENT_AUTHORITY, ProjectDB.TABLE_NAME, PROJECT)
        matcher.addURI(CONTENT_AUTHORITY, "${ProjectDB.TABLE_NAME}/#", PROJECT_ID)

        matcher.addURI(CONTENT_AUTHORITY, RelationsDB.TABLE_NAME, RELATIONS)

        return matcher
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun getType(uri: Uri): String {

        return when (uriMatcher.match(uri)) {

            STUDENTS -> StudentsDB.CONTENT_TYPE
            STUDENTS_ID -> StudentsDB.CONTENT_ITEM_TYPE

            SUBJECT -> SubjectDB.CONTENT_TYPE
            SUBJECT_ID -> SubjectDB.CONTENT_ITEM_TYPE

            PROJECT -> ProjectDB.CONTENT_TYPE
            PROJECT_ID -> ProjectDB.CONTENT_ITEM_TYPE

            RELATIONS -> RelationsDB.CONTENT_TYPE

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(
        uri: Uri, projection: Array<out String>?, selection: String?,
        selectionArgs: Array<out String>?, sortOrder: String?
    ): Cursor? {

        val match = uriMatcher.match(uri)

        val queryBuilder = SQLiteQueryBuilder()

        when (match) {

            STUDENTS -> queryBuilder.tables = StudentsDB.TABLE_NAME
            STUDENTS_ID -> {
                queryBuilder.tables = StudentsDB.TABLE_NAME
                val queryId = StudentsDB.getId(uri)
                queryBuilder.appendWhere("${StudentsDB.Columns.F_NUMBER} = ")
                queryBuilder.appendWhereEscapeString("$queryId")
            }

            SUBJECT -> queryBuilder.tables = SubjectDB.TABLE_NAME
            SUBJECT_ID -> {
                queryBuilder.tables = SubjectDB.TABLE_NAME
                val queryId = SubjectDB.getId(uri)
                queryBuilder.appendWhere("${SubjectDB.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$queryId")
            }

            PROJECT -> queryBuilder.tables = ProjectDB.TABLE_NAME
            PROJECT_ID -> {
                queryBuilder.tables = ProjectDB.TABLE_NAME
                val queryId = ProjectDB.getId(uri)
                queryBuilder.appendWhere("${ProjectDB.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$queryId")
            }

            RELATIONS -> queryBuilder.tables = RelationsDB.TABLE_NAME

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val context = context ?: throw NullPointerException("Context can't be null here")
        val db = AppDatabase.getInstance(context).readableDatabase
        val cursor =
            queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        Log.d(TAG, "query: rows in returned cursor = ${cursor.count}")

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val match = uriMatcher.match(uri)

        val recordId: Long
        val returnUri: Uri

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            STUDENTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(StudentsDB.TABLE_NAME, null, values)
                if (recordId != -1L) {
                    returnUri = StudentsDB.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            SUBJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(SubjectDB.TABLE_NAME,null,values)
                if (recordId != -1L) {
                    returnUri = SubjectDB.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            PROJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(ProjectDB.TABLE_NAME, null, values)
                if (recordId != -1L) {
                    returnUri = ProjectDB.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert, Uri was $uri")
                }
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        if (recordId > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }
        return returnUri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val match = uriMatcher.match(uri)

        val count: Int
        var selectionCriteria: String

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            STUDENTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(StudentsDB.TABLE_NAME, values, selection, selectionArgs)
            }
            STUDENTS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = StudentsDB.getId(uri)
                selectionCriteria = "${StudentsDB.Columns.F_NUMBER} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(StudentsDB.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            SUBJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(SubjectDB.TABLE_NAME, values, selection, selectionArgs)
            }
            SUBJECT_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = SubjectDB.getId(uri)
                selectionCriteria = "${SubjectDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()){
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(SubjectDB.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            PROJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(ProjectDB.TABLE_NAME, values, selection, selectionArgs)
            }

            PROJECT_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = ProjectDB.getId(uri)
                selectionCriteria = "${ProjectDB.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()){
                    selectionCriteria += "AND ($selection)"
                }
                count = db.update(ProjectDB.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }

            RELATIONS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(RelationsDB.TABLE_NAME, values, selection, selectionArgs)
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        if (count > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }

        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = uriMatcher.match(uri)

        val count: Int
        var selectionCriteria: String

        val context = context ?: throw NullPointerException("Context can't be null here")

        when (match) {
            STUDENTS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(StudentsDB.TABLE_NAME, selection, selectionArgs)
            }
            STUDENTS_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = StudentsDB.getId(uri)
                selectionCriteria = "${StudentsDB.Columns.F_NUMBER} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(StudentsDB.TABLE_NAME, selectionCriteria, selectionArgs)
            }

            SUBJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(SubjectDB.TABLE_NAME, selection, selectionArgs)
            }
            SUBJECT_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = SubjectDB.getId(uri)
                selectionCriteria = "${SubjectDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(SubjectDB.TABLE_NAME, selectionCriteria, selectionArgs)
            }

            PROJECT -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(ProjectDB.TABLE_NAME, selection, selectionArgs)
            }
            PROJECT_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = ProjectDB.getId(uri)
                selectionCriteria = "${ProjectDB.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += "AND ($selection)"
                }
                count = db.delete(ProjectDB.TABLE_NAME, selectionCriteria, selectionArgs)
            }


            RELATIONS -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(RelationsDB.TABLE_NAME, selection, selectionArgs)
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        if (count > 0) {
            context.contentResolver?.notifyChange(uri, null)
        }
        return count
    }
}