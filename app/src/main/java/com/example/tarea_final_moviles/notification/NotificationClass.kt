package com.example.tarea_final_moviles.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.tarea_final_moviles.R

class NotificationClass( private val context: Context) {
    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = context.getString(R.string.channel_name)
            val channelId = context.getString(R.string.channel_id)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val nm: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    fun generateNotification(title: String, message: String){
        val channelId = context.getString(R.string.channel_id)
        val largerIcon = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.digimon_data_icon
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.digimon_data_icon)
            .setLargeIcon(largerIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Si no tiene permiso, solicitarlo
                ActivityCompat.requestPermissions(context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            } else {
                // Si ya tiene permiso, mostrar la notificación
                notificationManager.notify(1, notification)
            }
        } else {
            // Para versiones anteriores a Android 13, simplemente mostrar la notificación
            notificationManager.notify(1, notification)
        }
    }
}