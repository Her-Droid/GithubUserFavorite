package com.russi.consumerapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.russi.consumerapp.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity: AppCompatActivity(){

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    companion object{
        const val PREFERENCE = "setting"
        const val ALARM = "alarm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.setting)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

        setSwitch()
        sw_alarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setAlarm(this,AlarmReceiver.ALARM_TITLE, getString(R.string.favorite_message))
            }else{
                alarmReceiver.cancelAlarm(this)
            }
            saveChangeFavorite(isChecked)
        }
    }

    private fun saveChangeFavorite(value: Boolean) {
        val editorInfo = sharedPreferences.edit()
        editorInfo.putBoolean(ALARM, value)
        editorInfo.apply()
    }

    private fun setSwitch() {
     sw_alarm.isChecked = sharedPreferences.getBoolean(ALARM, false)
    }
}