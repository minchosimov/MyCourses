package com.mincho.mycourses

import android.app.Activity
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.databinding.ActivityAddStudentsBinding
import com.mincho.mycourses.model.Student
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val ARG_PUT_STUDENT = "student"

class AddStudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentsBinding

    private var student: Student? = null

    private var isUpdate = false
    private var oldFacNumber: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        student = bundle?.getParcelable(ARG_PUT_STUDENT)

        if (student != null) {
            isUpdate = true
            oldFacNumber = student?.facNumber!!
        }

        if (savedInstanceState == null) {
            val student = student
            if (student != null) {
                binding.addEditFacultyNumber.setText(student.facNumber.toString())
                binding.addEditFirstName.setText(student.fName)
                binding.addEditSecondName.setText(student.sName)
                binding.addEditCourse.setText(student.course)

            }
        }


        binding.addEditSaveButton.setOnClickListener {
            saveStudent()
        }

    }

    private fun studentFromUI():Student{
        // !!!!
        val newStudent = Student(binding.addEditFacultyNumber.text.toString().toLong(),
            binding.addEditFirstName.text.toString(),
            binding.addEditSecondName.text.toString(),
            binding.addEditCourse.text.toString())

        return newStudent

    }

    private fun saveStudent() {
        val newStudent = studentFromUI()

        if (newStudent != null) { //!!!
            putStudent(newStudent)
            setResult(Activity.RESULT_OK)
            finish()
        }

        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun putStudent(newStudent: Student){

        val values = ContentValues()
        values.put(StudentsDB.Columns.F_NUMBER, newStudent.facNumber)
        values.put(StudentsDB.Columns.F_NAME, newStudent.fName)
        values.put(StudentsDB.Columns.S_NAME, newStudent.sName)
        values.put(StudentsDB.Columns.COURSE, newStudent.course)

        if (!isUpdate){
            GlobalScope.launch {
                val uri =
                    application.contentResolver?.insert(
                        StudentsDB.CONTENT_URI,
                        values
                    )
            }
        } else {
            GlobalScope.launch {
                application.contentResolver?.update(
                    StudentsDB.buildUriFromId(oldFacNumber),values,null,null)
            }
        }
    }
}