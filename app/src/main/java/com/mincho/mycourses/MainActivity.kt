package com.mincho.mycourses

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mincho.mycourses.database.StudentsDB
import com.mincho.mycourses.databinding.ActivityMainBinding
import com.mincho.mycourses.model.Student

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),
    ShowStudentsFragment.OnStudentsListener {
    lateinit var binding: ActivityMainBinding //to connect with xml

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menuMain_newStudent -> {
                startActivity(Intent(this,AddStudentsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStudentsEdit(student: Student) {
        val intent = Intent(this, AddStudentsActivity::class.java)
        intent.putExtra(ARG_PUT_STUDENT,student)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}