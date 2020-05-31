package com.teme.alarmmanager

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_one.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentOne : Fragment() {

    private lateinit var alarmManager: AlarmManager

    private val cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()

        btn_choose_time.setOnClickListener {
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    tv_time_picked.text = SimpleDateFormat("HH:mm").format(cal.time)
                    //tv_time_picked.text = cal.time.toString()
                }
            TimePickerDialog(
                activity,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        btn_choose_date.setOnClickListener {
            //val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    //tv_date_picked.text = SimpleDateFormat("dd/mm/yy").format(cal.time)
                    tv_date_picked.text = "$dayOfMonth/$month/$year"
                }
            DatePickerDialog(
                context!!,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btn_create_alarm.setOnClickListener {
            Toast.makeText(context, "Reminder set at " +
                    cal.get(Calendar.HOUR_OF_DAY).toString() + ":" +
                    cal.get(Calendar.MINUTE).toString() + " " +
                    cal.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                    cal.get(Calendar.MONTH).toString() + "/" +
                    cal.get(Calendar.YEAR).toString()
                , Toast.LENGTH_SHORT).show()

            val contentIntent = Intent(activity!!, ReminderBroadcast::class.java)
            val contentPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                contentIntent,
                0
            )

            alarmManager = (context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)!!

            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,
                contentPendingIntent
            )
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification_channel"
            val description = "Channel notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notification_channel", name, importance)
            channel.description = description

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
