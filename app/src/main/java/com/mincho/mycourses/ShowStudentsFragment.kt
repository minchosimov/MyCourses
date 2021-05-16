package com.mincho.mycourses

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mincho.mycourses.databinding.FragmentShowStudentsBinding
import com.mincho.mycourses.model.Student

/**
 * A simple [Fragment] subclass.
 * Use the [ShowStudentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 *При използването на фрагменти, AndroidStudio създава съответно двата файла този, програмен и XML
 * Както ще забележите в този програмен файл се предифира определена структура наследена от Fragments
 * Тук конткретно ние наследяване още две структури
 */


class ShowStudentsFragment : Fragment(),
        View.OnClickListener,
        StudentsListRVAdapter.OnStudentsClickListener{

    // Този интефейс е дефиниран в MainActivity, чрез който обработваме събитието описано там
    interface OnStudentsListener{
        fun onStudentsEdit(student: Student)
    }

    private var _binding: FragmentShowStudentsBinding? = null
    private val binding get() =  _binding!!

    //следвщата променлива е от MVVM архитектурата и прави връзката между бизнез модела и потребителския интерфейс
    private val viewModel by lazy { ViewModelProvider(this).get(StudentsViewModel::class.java)}

    //следващата пробемлива дефинира инстанция на класа, който се грижи за обработна на RecycleView
    private val mAdapter = StudentsListRVAdapter(null,this)

    private var cursor: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.cursor.observe(this, { cursor -> mAdapter.swapCursor(cursor)?.close() })
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

        binding.studentsList.layoutManager = LinearLayoutManager(context)
        binding.studentsList.adapter = mAdapter

        //set onClickListener for heading

        binding.tdFacNumberHeading.setOnClickListener(this)
        binding.tdNameHeading.setOnClickListener(this)
        binding.tdCourseHeading.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.td_facNumber_heading -> viewModel.sortOrder = SortColumns.FACULTY_NUMBER
            R.id.td_name_heading -> viewModel.sortOrder = SortColumns.NAME
            R.id.td_course_heading -> viewModel.sortOrder = SortColumns.COURSE
        }
    }

    override fun onDeleteClick() {
        TODO("Not yet implemented")
    }

    override fun onEditClick(student: Student): Boolean {
        (activity as OnStudentsListener).onStudentsEdit(student)
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