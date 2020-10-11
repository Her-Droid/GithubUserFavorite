package com.russi.githubapi.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.russi.githubapi.MainActivity
import com.russi.githubapi.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ALARM_TITLE = "Daily Reminder"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        private const val ALARM_ID = 100
        private const val ALARM_TIME = "09:00"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        alarmNotification(context, message)
    }

    fun setAlarm(context: Context?, type: String?, message: String?) {
        val alarmManger = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)
        val time = ALARM_TIME.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]))
        calender.set(Calendar.MINUTE, Integer.parseInt(time[1]))
        calender.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context, ALARM_ID, intent, PendingIntent.FLAG_ONE_SHOT
        )
        alarmManger.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, context.getString(R.string.alarm), Toast.LENGTH_SHORT).show()
    }

    private fun alarmNotification(context: Context?, message: String?) {
        val channelId = "GithubApi"
        val channelName = "Github Notification"
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_arrow_forward_ios_24).setContentTitle("Alarm")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = builder.build()
        notificationManager.notify(100, notification)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ALARM_ID
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, context.getString(R.string.alarm_cancel), Toast.LENGTH_SHORT).show()
    }

}