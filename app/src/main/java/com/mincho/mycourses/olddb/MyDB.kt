package com.mincho.mycourses.olddb

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *Created by Mincho Simov on 05/02/2021.
 */
class myDB(private val context: Context) {
    private lateinit var mDbHelper: DatabaseHelper
    private lateinit var mDb: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(): myDB {
        mDbHelper = DatabaseHelper(context)
        mDb = mDbHelper.writableDatabase
        return this
    }

    fun close() {
        mDbHelper.close()
    }

    fun insertNewStudents (){
        val values = ContentValues()
        values.put("FAK_NOMER",200012356)
        values.put("F_NAME", "Иван")
        values.put("S_NAME","Димитров")
        mDb.insert("STUDENTS",null, values)

    }
    fun insertSpec(){
        val values = ContentValues()
        values.put("NAME", "Android")
        mDb.insert("SPEC", null, values)
    }

    fun insertDisciplina(){
        val values = ContentValues()
        values.put("NAME", "Програмиране за Android")
        values.put("YEAR",2021)
        values.put("SEMESTAR",2)
        mDb.insert("DISCIPLINA", null, values)
    }


    private class DatabaseHelper internal constructor(context: Context) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_TABLE_STUDENTS)
            db.execSQL(SQL_CREATE_TABLE_SPEC)
            db.execSQL(SQL_CREATE_TABLE_PROJECT)
            db.execSQL(SQL_CREATE_TABLE_DISCIPLINA)
            db.execSQL(SQL_CREATE_TABLE_RELATION)

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

            if (newVersion > oldVersion) {
                db.execSQL(SQL_DELETE_STUDENTS)
                db.execSQL(SQL_DELETE_SPEC)
                db.execSQL(SQL_DELETE_PROJECT)
                db.execSQL(SQL_DELETE_DISCIPLINA)
                db.execSQL(SQL_DELETE_RELATION)

                onCreate(db)
            } else {
                // otherwise, create the database
                onCreate(db)
            }
        }
    }

    companion object{
        const val DATABASE_NAME = "mycourses"
        const val DATABASE_VERSION = 1

        const val SQL_CREATE_TABLE_STUDENTS =
                "CREATE TABLE STUDENTS (" +
                        "FAK_NOMER INTEGER PRIMARY KEY, " +
                        "F_NAME VARCHAR(20) NOT NULL, " +
                        "S_NAME VARCHAR(20) NOT NULL)"
        const val SQL_DELETE_STUDENTS = "DROP TABLE IF EXISTS STUDENTS"

        const val SQL_CREATE_TABLE_SPEC =
                "CREATE TABLE SPEC (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(30) NOT NULL, " +
                        "UNIQUE (NAME) ON CONFLICT IGNORE )"
        const val SQL_DELETE_SPEC = "DROP TABLE IF EXISTS SPEC"

        const val SQL_CREATE_TABLE_PROJECT =
                "CREATE TABLE PROJECT (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(40) NOT NULL, " +
                        "OPISANIE VARCHAR(50), " +
                        "REITING INTEGER )"
        const val SQL_DELETE_PROJECT = "DROP TABLE IF EXISTS PROJECT"

        const val SQL_CREATE_TABLE_DISCIPLINA =
                "CREATE TABLE DISCIPLINA (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "NAME VARCHAR(50) NOT NULL," +
                        "YEAR INTEGER, " +
                        "SEMESTAR INTEGER )"
        const val SQL_DELETE_DISCIPLINA = "DROP TABLE IF EXISTS DISCIPLINA"

        const val SQL_CREATE_TABLE_RELATION =
                "CREATE TABLE RELATION (" +
                        "ID_D INTEGER NOT NULL, " +
                        "ID_S INTEGER NOT NULL, " +
                        "ID_P INTEGER NOT NULL, " +
                        "KURS INTEGER NOT NULL, " +
                        "FOREIGN KEY(ID_D) REFERENCES DISCIPLINA(ID) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "FOREIGN KEY(ID_S) REFERENCES STUDENTS(FAK_NOMER) ON DELETE CASCADE ON UPDATE CASCADE, " +
                        "FOREIGN KEY(ID_P) REFERENCES PROJECT(ID) ON DELETE CASCADE ON UPDATE CASCADE) "
        const val SQL_DELETE_RELATION = "DROP TABLE IF EXISTS RELATION"
    }
}