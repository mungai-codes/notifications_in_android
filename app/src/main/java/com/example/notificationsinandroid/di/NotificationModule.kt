package com.example.notificationsinandroid.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.notificationsinandroid.MainActivity
import com.example.notificationsinandroid.R
import com.example.notificationsinandroid.navigation.MY_ARG
import com.example.notificationsinandroid.navigation.MY_URI
import com.example.notificationsinandroid.receiver.MyReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    //to define how our notification actually looks like
    @Provides
    @Singleton
    @MainNotificationCompatBuilder
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {

        val intent =
            Intent(context, MyReceiver::class.java).apply { putExtra("message", "Clicked") }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag)
//        val clickIntent = Intent(context, MainActivity::class.java)
//        val clickPendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            clickIntent,
//            flag
//        )
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$MY_ARG=Coming from Notification".toUri(),
            context,
            MainActivity::class.java
        )
        val clickPendingIntent: PendingIntent = TaskStackBuilder.create(context)
            .run { addNextIntentWithParentStack(clickIntent).getPendingIntent(1, flag) }

        return NotificationCompat.Builder(
            context,
            "Main Channel Id"
        )//takes in the context and our channel id.
            //providing the channel id is a requirement for compatibility with api 26 or higher.(ignored by older versions)
            .setContentTitle("Welcome")
            .setContentText("Youtube Channel: Stevdza-San")//subtitle.
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)//used for devices api 25 and lower for 26 and over notification importance is defined in the channel.
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context, "Main Channel Id")
                    .setContentTitle("Public Version")
                    .setContentText("Unlock to see content")
                    .build()
            )
            .addAction(0, "ACTION", pendingIntent)
            .setContentIntent(clickPendingIntent)
    }

    @Provides
    @Singleton
    @SecondNotificationCompatBuilder
    fun provideSecondNotificationBuilder(
        @ApplicationContext context: Context

    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "Second Channel Id")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

    }

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {

        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Main Channel Id",//should be same as one provided in the notification builder.
                "Main Channel",//visible within user settings
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channel2 = NotificationChannel(
                "Second Channel Id",//should be same as one provided in the notification builder.
                "Second Channel",//visible within user settings
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)
        }
        return notificationManager
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainNotificationCompatBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecondNotificationCompatBuilder