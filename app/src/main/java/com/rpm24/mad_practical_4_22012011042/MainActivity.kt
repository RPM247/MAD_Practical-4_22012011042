package com.rpm24.mad_practical_4_22012011042

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun getCurrentDateTime():String{
        val cal=Calendar.getInstance()
        val df:DateFormat = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a")
        return df.format(cal.time)
    }
    fun getMillis(hour:Int, min:Int): Long {
        val setcalendar=Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY]=hour
        setcalendar[Calendar.MINUTE]=min
        setcalendar[Calendar.SECOND]=0
        return setcalendar.timeInMillis
    }
    fun showTimerDialog(){
        val cldr=Calendar.getInstance()
        val hour:Int=cldr.get(Calendar.HOUR_OF_DAY)
        val minutes:Int=cldr.get(Calendar.MINUTE)
        val picker=TimePickerDialog(
            this,
            {tp, sHour, sMinute -> sendDialogDataToActivity(sHour, sMinute)},
            hour,
            minutes,
            false
        )
    }
    private fun sendDialogDataToActivity(Hour: Int, Minute: Int) {
        val alarmCalendar=Calendar.getInstance()
        val year:Int=alarmCalendar.get(Calendar.YEAR)
        val month:Int=alarmCalendar.get(Calendar.MONTH)
        val day:Int=alarmCalendar.get(Calendar.DATE)
        alarmCalendar.set(year, month, day, Hour, Minute, 0)
        //setAlarm(alarmCalendar.timeInMillis, "Start")
    }
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(millisTime:Long, str:String){
        val intent=Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent=PendingIntent.getBroadcast(applicationContext, 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
        if(str=="Start"){
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
            Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show()
        }
        else if(str=="Stop"){
            alarmManager.cancel(pendingIntent)
            setBroadcast(intent);
            Toast.makeText(this, "Alarm Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}