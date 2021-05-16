package com.mincho.mycourses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mincho.mycourses.databinding.ActivityMainBinding
import com.mincho.mycourses.model.Student

//Главното активити, то се показва веднага след зареждане на приложението от съответното устройство
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),
    ShowStudentsFragment.OnStudentsListener {

    private lateinit var binding: ActivityMainBinding //to connect with xml

    /**
     * Както разгледахме цикличния живот на едно активири и етапите през които минава.
     * Следващите наследение функции, които пренаписваме са именно за контрол на изпълнението,
     * през различните етапи:
     * Създаване
     * Стартиране
     * Създаване на меню
     * Контрол над менюто
     * и най-накравя унищожаване на активитито
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG,"OnCreate" )
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



    }

    override fun onStart() {
        Log.e(TAG,"On Start")
        super.onStart()
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
            R.id.menuMain_settings -> {
                val dialog = SettingsDialog()
                dialog.show(supportFragmentManager,null)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**Тази функция е наследена от ShowStudentsFragment.OnStudentsListener
     * Чрез нея ние дефинираме фукцията от интерфейса за обработка на събитие при натискане върху
     * сътоветен студент от листа с всички студенти, въведени в базата данни
     */
    override fun onStudentsEdit(student: Student) {
        val intent = Intent(this, AddStudentsActivity::class.java)
        intent.putExtra(ARG_PUT_STUDENT,student)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}