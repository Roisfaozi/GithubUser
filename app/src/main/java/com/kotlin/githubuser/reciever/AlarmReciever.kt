package com.kotlin.githubuser.reciever

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import com.kotlin.githubuser.R
import com.kotlin.githubuser.activity.MainActivity
import java.util.*

class AlarmReciever : BroadcastReceiver(){
     companion object{
         const val TYPE_REPEATING = "RepeatingAlarm"
         const val EXTRA_MESSAGE = "message"
         const val EXTRA_TYPE = "type"

         private const val ID_REPEAT = 101

     }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val notifId = if( type.equals(TYPE_REPEATING, ignoreCase = true) ) ID_REPEAT else return
        showAlarmNotification(context, message, notifId)
    }

    private fun showAlarmNotification(context: Context, message: String, notifId: Int){
        val channlId = "CH01"
        val channelName = "Daily Reminder"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0 , intent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channlId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.daily_reminder))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channlId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channlId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notificationBuilder = builder.build()
        notificationManagerCompat.notify(notifId, notificationBuilder)

    }

    fun setAlarm(context: Context, type: String, message: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReciever::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_TYPE, type)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEAT, intent, 0)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val time = calendar.timeInMillis - System.currentTimeMillis()
        if (time > 0){
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else{
            val mTime = System.currentTimeMillis() + AlarmManager.INTERVAL_DAY - time
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                mTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        Toast.makeText(context, "Alarm Active", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciever::class.java)
        val requestCode = if (type.equals(TYPE_REPEATING, ignoreCase = true)) ID_REPEAT else 0
        val pendingIntnt = PendingIntent.getBroadcast(context, requestCode, intent, 0)

        pendingIntnt.cancel()
        alarmManager.cancel(pendingIntnt)
        Toast.makeText(context, "Alarm Of", Toast.LENGTH_SHORT).show()
    }

    fun isAlarmSet(context: Context, type: String): Boolean {
        val intent = Intent(context, AlarmReciever::class.java)
        val requestCode = if (type.equals(TYPE_REPEATING, ignoreCase = true)) ID_REPEAT else 0
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }
}