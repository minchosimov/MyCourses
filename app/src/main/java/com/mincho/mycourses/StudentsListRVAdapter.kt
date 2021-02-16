package com.mincho.mycourses

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.databinding.StudentsListLayoutBinding
import com.mincho.mycourses.model.Student
import java.security.acl.Owner

/**
 *Created by Mincho Simov on 16/02/2021.
 */
class StudentsViewHolder(private val holder: StudentsListLayoutBinding) :
    RecyclerView.ViewHolder(holder.root) {



    fun bind(student: Student, listener: StudentsListRVAdapter.OnStudentsClickListener){

        holder.tvFacNumberStudentsList.text = student.facNumber.toString()
        val name = "${student.fName} ${student.sName}"
        holder.tvNameStudentsList.text = name
        holder.tvCourseStudentsList.text = student.course

        holder.itemLayoutStudentsList.setOnLongClickListener {
            listener.onEditClick(student)
        }
    }
}

class StudentsListRVAdapter (private var cursor: Cursor?, private val listener: OnStudentsClickListener) :
    RecyclerView.Adapter<StudentsViewHolder>() {

        interface OnStudentsClickListener{
            fun onDeleteClick() //TODO
            fun onEditClick(student: Student):Boolean
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
            val holder = StudentsListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            return StudentsViewHolder(holder)
        }

        override fun getItemCount(): Int {
            return cursor?.count ?: 0
        }

        override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
            val cursor = cursor

            if (cursor != null && cursor.count != 0){
                if (!cursor.moveToPosition(position)){
                    throw IllegalStateException("Couldn't move cursor to position $position")
                }
                val item = Student(cursor.getLong(cursor.getColumnIndex(StudentsDB.Columns.F_NUMBER)),
                cursor.getString(cursor.getColumnIndex(StudentsDB.Columns.F_NAME)),
                cursor.getString(cursor.getColumnIndex(StudentsDB.Columns.S_NAME)),
                cursor.getString(cursor.getColumnIndex(StudentsDB.Columns.COURSE)))

                holder.bind(item, listener)
            }

        }

        fun swapCursor(newCursor: Cursor?): Cursor?{
            if (newCursor === cursor){
                return null
            }
            val numItems = itemCount
            val oldCursor = cursor
            cursor = newCursor
            if (newCursor != null){
                notifyDataSetChanged()
            } else {
                notifyItemRangeRemoved(0,numItems)
            }

            return oldCursor
        }
    }