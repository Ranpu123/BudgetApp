package com.example.budgetapp.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.budgetapp.App
import com.example.budgetapp.R

class SyncNotificationService(private val context: Context) {

    private var onGoing = false
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun showNotification(){
        onGoing = true
        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_updating)
            .setContentTitle(context.getText(R.string.notification_title))
            .setContentText(context.getText(R.string.notification_text))
            .setOngoing(onGoing)
            .build()

        notificationManager.notify(1, notification)
    }

    fun hideNotification(){
        onGoing = false
        notificationManager.cancel(1)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                SYNC_CHANNEL_ID,
                SYNC_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Usado para informar quando dados estão sendo sincronizados com o servidor."

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        const val SYNC_CHANNEL_NAME = "Sincronização de Dados"
        const val SYNC_CHANNEL_ID = "sync_channel"
    }
}