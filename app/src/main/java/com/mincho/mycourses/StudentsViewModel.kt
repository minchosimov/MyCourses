package com.mincho.mycourses

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.model.Student
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *Created by Mincho Simov on 18/02/2021.
 *
 * * This is the ViewModel class
 * This class is part of MVVM model
 * It access the database through AppProvider
 *
 * Този клас е основна част от MVVM архитектурата
 * чрез него се доступва базата данни, двупосочно
 *
 * Другата интересна структура е използването на GlobalScope
 * Тя е част от Coroutines на Kotlin, чрез който с доста по-малко ресурси могат да се сътрартират
 * асинхрони незвисими заявки
 */

private const val TAG  = "Students ViewModel"

enum class SortColumns{
    FACULTY_NUMBER,
    NAME,
    COURSE
}

class StudentsViewModel(application: Application) : AndroidViewModel(application) {

    private val contentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            loadStudents()
        }
    }

    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
        get() = databaseCursor


    var sortOrder = SortColumns.FACULTY_NUMBER
        set(order){
            if (field != order){
                field = order
                loadStudents()
            }
        }

    init {
        getApplication<Application>().contentResolver.registerContentObserver(
                StudentsDB.CONTENT_URI,
                true, contentObserver
        )
        loadStudents()

    }

    private fun loadStudents() {
        val order = when (sortOrder){
            SortColumns.FACULTY_NUMBER -> StudentsDB.Columns.F_NUMBER
            SortColumns.NAME -> StudentsDB.Columns.F_NAME
            SortColumns.COURSE -> StudentsDB.Columns.COURSE
        }

        GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver
                    .query(StudentsDB.CONTENT_URI, null, null, null, order)
            databaseCursor.postValue(cursor)
        }
    }

    fun putStudent(newStudent: Student, isUpdate: Boolean, oldFacNumber: Long) {
        val values = ContentValues()
        values.put(StudentsDB.Columns.F_NUMBER, newStudent.facNumber)
        values.put(StudentsDB.Columns.F_NAME, newStudent.fName)
        values.put(StudentsDB.Columns.S_NAME, newStudent.sName)
        values.put(StudentsDB.Columns.COURSE, newStudent.course)

        if (!isUpdate){
            GlobalScope.launch {
                val uri =
                        getApplication<Application>().contentResolver?.insert(
                                StudentsDB.CONTENT_URI,
                                values
                        )
            }
        } else {
            GlobalScope.launch {
                getApplication<Application>().contentResolver?.update(
                        StudentsDB.buildUriFromId(oldFacNumber),values,null,null)
            }
        }
    }

    override fun onCleared() {
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }
}
