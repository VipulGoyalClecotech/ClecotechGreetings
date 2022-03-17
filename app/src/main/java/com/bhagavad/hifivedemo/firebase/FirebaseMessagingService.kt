package com.bhagavad.hifivedemo.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.isDigitsOnly
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.bhagavad.hifivedemo.R


class FirebaseMessagingService : FirebaseMessagingService() {

    val TAG: String by lazy {
        "FirebaseMessagingService"
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From data1: ${remoteMessage.notification!!.title}")
        Log.d(TAG, "From data2 ${remoteMessage.notification!!.body}")
        Log.d(TAG, "From data3: ${remoteMessage.data}")
        Log.d(TAG, "From data4: ${remoteMessage.notification}")
        val body=remoteMessage.notification!!.body
        sendNotification(remoteMessage.notification!!.title,body,body,body,body)

        if (remoteMessage.data.size > 0) {
            val message = remoteMessage.data["body"]
        //    val title = remoteMessage.data["title"]

            Log.d(TAG, "From_data_C: ${message}")

         }
    }

    //for class end {notification_setting=, body=Class is ended by teacher., icon=icon, type=9, badge=1, sound=push_short_duration.caf, title=Class Ended, click_action=FCM_PLUGIN_ACTIVITY, class_id=32}

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(title: String?, messageBody: String?,
                                 type: String?,classId: String?,teacherId: String?) {

        var id = System.currentTimeMillis().toInt() //150;

        /*var intent = Intent(this, SplashActivity::class.java)
        if (type!=null&&type.isDigitsOnly()&&type.trim().equals("9")){
            intent= Intent(this, TeacherClassFeedbackActivity::class.java)
            intent.putExtra("class_id",classId)
            intent.putExtra("teacher_id",teacherId)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0  *//*Request code*//*, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
*/

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
           // .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()
            )
            channel.enableVibration(true)
            channel.setVibrationPattern(longArrayOf(100, 200, 100, 200, 100))

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id  /*ID of notification*/, notificationBuilder.build())
    }
}