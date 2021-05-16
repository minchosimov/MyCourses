package com.mincho.mycourses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment

import androidx.preference.PreferenceManager
import com.mincho.mycourses.databinding.SettingsDialogBinding

/**
 *Created by Mincho Simov on 25/01/2021.
 */


class SettingsDialog : AppCompatDialogFragment() {

    private var _binding: SettingsDialogBinding? = null
    private val binding  get() = _binding!!

    private var isNotifyActive = true
    private var hour = 12
    private var minutes = 10



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsDialogBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsDialogNotificationSwitch.setOnClickListener { refresh() }
         binding.settingsDialogSaveButton.setOnClickListener {
             saveValues()
             dismiss()
         }

        binding.settingsDialogCancelButton.setOnClickListener {
            dismiss()
        }

    }

    private fun saveValues(){

        val isChecked = binding.settingsDialogNotificationSwitch.isChecked
        with(PreferenceManager.getDefaultSharedPreferences(context).edit()){
            if (isNotifyActive != isChecked ){
                putBoolean(NOTIFICATION_ALLOW_TEXT,isChecked)
            }
            apply()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState == null){
            readValues()

            binding.settingsDialogNotificationSwitch.isChecked = isNotifyActive

            refresh()



        }
    }

    private fun readValues(){
        with( PreferenceManager.getDefaultSharedPreferences(context)){
            isNotifyActive = getBoolean(NOTIFICATION_ALLOW_TEXT,true)
            hour = getInt(NOTIFICATION_HOUR,12)
            minutes = getInt(NOTIFICATION_MINUTES, 10)
        }
    }

    private fun refresh(){
        if (binding.settingsDialogNotificationSwitch.isChecked){
            binding.textView22.visibility = View.VISIBLE
            binding.timeForNotify.visibility = View.VISIBLE
            if (!((hour == 12) && (minutes == 10))){
                val time = "$hour : $minutes"
                binding.timeForNotify.text = time
            }
        } else{
            binding.textView22.visibility = View.GONE
            binding.timeForNotify.visibility = View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val NOTIFICATION_ALLOW_TEXT = "NotifyAllow"
        const val NOTIFICATION_HOUR = "time"
        const val NOTIFICATION_MINUTES = "minutes"
    }
}