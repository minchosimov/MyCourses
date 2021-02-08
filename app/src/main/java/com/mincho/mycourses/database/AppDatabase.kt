package com.mincho.mycourses.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mincho.mycourses.utilities.SingletonHolder

/**
 *Created by Mincho Simov on 04/02/2021.
 * Basic database class for the application
 * The only class that should use this class is [AppProvider]
 */
//private const val TAG = "AppDatabaseClass"

private const val DATABASE_NAME = "MyCourses.db"
private const val DATABASE_VERSION = 1
//table
private val CREATE_STUDENTS_TABLE_SQL = """CREATE TABLE ${StudentsDB.TABLE_NAME} (
        ${StudentsDB.Columns.F_NUMBER} INTEGER PRIMARY KEY,
        ${StudentsDB.Columns.F_NAME} VARCHAR(20) NOT NULL,
        ${StudentsDB.Columns.S_NAME} VARCHAR(20) NOT NULL,
        ${StudentsDB.Columns.COURSE} VARCHAR(30) NOT NULL)""".replaceIndent(" ")

private val CREATE_SUBJECT_TABLE_SQL = """CREATE TABLE ${SubjectDB.TABLE_NAME} ( 
    ${SubjectDB.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
    ${SubjectDB.Columns.NAME} VARCHAR(50) NOT NULL,
    ${SubjectDB.Columns.YEAR} INTEGER NOT NULL,
    ${SubjectDB.Columns.TERM} INTEGER NOT NULL)""".replaceIndent(" ")

private val CREATE_PROJECT_TABLE_SQL = """CREATE TABLE ${ProjectDB.TABLE_NAME} (
    ${ProjectDB.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
    ${ProjectDB.Columns.NAME} VARCHAR(50) NOT NULL,
    ${ProjectDB.Columns.DESCRIPTION} VARCHAR NOT NULL,
    ${ProjectDB.Columns.RATING} INTEGER DEFAULT 0)""".replaceIndent(" ")

private val CREATE_RELATIONS_TABLE_SQL = """CREATE TABLE ${RelationsDB.TABLE_NAME}( 
    ${RelationsDB.Columns.ID_STUDENT} INTEGER NOT NULL,
    ${RelationsDB.Columns.ID_SUBJECT} INTEGER NOT NULL,
    ${RelationsDB.Columns.ID_PROJECT} INTEGER NOT NULL,
    ${RelationsDB.Columns.STUDENT_YEAR} INTEGER NOT NULL,
    FOREIGN KEY(${RelationsDB.Columns.ID_STUDENT}) REFERENCES ${StudentsDB.TABLE_NAME}(${StudentsDB.Columns.F_NUMBER}) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(${RelationsDB.Columns.ID_SUBJECT}) REFERENCES ${SubjectDB.TABLE_NAME}(${SubjectDB.Columns.ID}) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(${RelationsDB.Columns.ID_PROJECT}) REFERENCES ${ProjectDB.TABLE_NAME}(${ProjectDB.Columns.ID}) ON DELETE CASCADE ON UPDATE CASCADE)""".replaceIndent(" ")


internal class AppDatabase (context : Context): SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_STUDENTS_TABLE_SQL)
        db.execSQL(CREATE_SUBJECT_TABLE_SQL)
        db.execSQL(CREATE_PROJECT_TABLE_SQL)
        db.execSQL(CREATE_RELATIONS_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (newVersion > oldVersion) {
            onCreate(db)

        } else {
            // otherwise, create the database
            onCreate(db)
        }
    }

    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)

}