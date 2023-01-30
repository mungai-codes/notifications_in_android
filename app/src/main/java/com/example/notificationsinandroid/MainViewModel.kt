package com.example.notificationsinandroid

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
) : ViewModel() {


    //creating notifications
    @SuppressLint("MissingPermission")
    fun showSimpleNotification() {
        notificationManager.notify(1, notificationBuilder.build())
    }

    @SuppressLint("MissingPermission")
    fun updateNotification() {
        notificationManager.notify(1, notificationBuilder.setContentTitle("Updated Title").build())

    }

    @SuppressLint("MissingPermission")
    fun cancelNotification() {
        notificationManager.cancel(1)

    }

}