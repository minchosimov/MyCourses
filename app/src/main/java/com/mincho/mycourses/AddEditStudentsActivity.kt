package com.mincho.mycourses

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mincho.mycourses.databinding.ActivityAddStudentsBinding
import com.mincho.mycourses.model.Student

const val ARG_PUT_STUDENT = "student"

/** Активитито, което се грижи за добавяне или редактиране на данните за един студент.
 * Тези дейности се извършват от потребителя
 */

class AddStudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentsBinding

    private val viewModel by lazy { ViewModelProvider(this).get(StudentsViewModel::class.java)}

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


    //Следващата функция извлича информацията въведена от потребителя
    private fun studentFromUI():Student{
        return Student(binding.addEditFacultyNumber.text.toString().toLong(),
            binding.addEditFirstName.text.toString(),
            binding.addEditSecondName.text.toString(),
            binding.addEditCourse.text.toString())

    }

    //Функцията записва извлечената информация в базата данни
    private fun saveStudent() {
        val newStudent = studentFromUI()

        //!!! Помислете какво липсва
        viewModel.putStudent(newStudent,isUpdate,oldFacNumber)
        setResult(Activity.RESULT_OK)
        finish()

        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}