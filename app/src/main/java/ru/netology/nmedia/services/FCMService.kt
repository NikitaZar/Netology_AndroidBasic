package ru.netology.nmedia.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.netology.nmedia.R
import com.google.firebase.messaging.*
import com.google.gson.Gson
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    companion object {
        private const val action = "action"
        private const val content = "content"
        private const val channelId = "netology"
        private val actionMap = mapOf("LIKE" to Action.LIKE, "PUBLISHED" to Action.PUBLISHED)
    }

    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        Log.i("token", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {

        message.data[action]?.let {
            when (actionMap[it]) {
                Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                Action.PUBLISHED -> handlePublished(
                    gson.fromJson(
                        message.data[content],
                        Published::class.java
                    )
                )
                else -> return
            }
        }
    }

    private fun handleLike(like: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_netology)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor
                )
            )
            .build()
        NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
    }

    private fun handlePublished(published: Published) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_netology)
            .setContentTitle(
                getString(
                    R.string.notification_published,
                    published.postAuthor
                )
            )
            .setContentText(published.postText)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(published.postText)
            )
            .build()

        NotificationManagerCompat.from(this).notify(
            Random.nextInt(100_000),
            notification
        )
    }
}