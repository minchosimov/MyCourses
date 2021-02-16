package com.mincho.mycourses

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.databinding.FragmentShowStudentsBinding
import com.mincho.mycourses.model.Student

/**
 * A simple [Fragment] subclass.
 * Use the [ShowStudentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

enum class SortColumns{
    FACULTY_NUMBER,
    NAME,
    COURSE
}

class ShowStudentsFragment : Fragment(),
    StudentsListRVAdapter.OnStudentsClickListener{

    interface OnStudentsListener{
        fun onStudentsEdit(student: Student)
    }

    private var _binding: FragmentShowStudentsBinding? = null
    private val binding get() =  _binding!!

    private val mAdapter = StudentsListRVAdapter(null,this)

    private var sortOrder = SortColumns.FACULTY_NUMBER

    private var cursor: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadStudents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentShowStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.carList.layoutManager = LinearLayoutManager(context)
        binding.carList.adapter = mAdapter

        //set onClickListener for heading
        binding.tdFacNumberHeading.setOnClickListener{
            sortOrder = SortColumns.FACULTY_NUMBER
            loadStudents()

        }
        binding.tdNameHeading.setOnClickListener{
            sortOrder = SortColumns.NAME
            loadStudents()
        }
        binding.tdCourseHeading.setOnClickListener{
            sortOrder = SortColumns.COURSE
            loadStudents()
        }

    }

    private fun loadStudents(){
        val order = when (sortOrder){
            SortColumns.FACULTY_NUMBER -> StudentsDB.Columns.F_NUMBER
            SortColumns.NAME -> StudentsDB.Columns.F_NAME
            SortColumns.COURSE -> StudentsDB.Columns.COURSE
        }
        cursor = activity?.contentResolver
            ?.query(StudentsDB.CONTENT_URI, null, null, null, order)
        mAdapter.swapCursor(cursor)
    }

    override fun onDeleteClick() {
        TODO("Not yet implemented")
    }

    override fun onEditClick(student: Student): Boolean {
        (activity as OnStudentsListener)?.onStudentsEdit(student)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ShowStudentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ShowStudentsFragment()
    }
}