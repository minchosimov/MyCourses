package com.mincho.mycourses

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.olddb.myDB

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //oldDB
        val myDB = myDB(this)
        myDB.open()
        myDB.insertNewStudents()
        myDB.insertSpec()
        myDB.insertDisciplina()
        myDB.close()

        //content provider
        val values = ContentValues()
        values.put(StudentsDB.Columns.F_NUMBER,1257892)
        values.put(StudentsDB.Columns.F_NAME, "Иван")
        values.put(StudentsDB.Columns.S_NAME,"Димитров")
        values.put(StudentsDB.Columns.COURSE,"Информатика")

        val id = this.application.contentResolver.insert(StudentsDB.CONTENT_URI,values)


        val cursor: Cursor? =
            this.application.contentResolver.query(StudentsDB.CONTENT_URI, null, null, null,null )

        Log.v(TAG,"${cursor?.count}")
        cursor?.close()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}