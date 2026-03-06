package org.russel.komandoandroid.fcmservice

import android.R
import android.app.NotificationManager
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.remote.RetrofitClient
import org.russel.komandoandroid.data.repository.DeviceRepository

/**
 * Handles incoming FCM messages and token refresh events.
 *
 * Runs even when:
 * - App is in background
 * - App is killed
 */
class KomandoFirebaseMessagingService() : FirebaseMessagingService() {
    // ======================================================================================== //
    private val deviceRepository by lazy {
        DeviceRepository(RetrofitClient.deviceService(applicationContext))
    }
    // ======================================================================================== //
    /**
     * Called whenever a message is received from Firebase.
     * Used automatically by Firebase
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title: String? = null
        var body: String? = null

        // 1️⃣ Handle notification payload
        remoteMessage.notification?.let { notification ->
            title = notification.title
            body = notification.body
            Log.d(TAG, "Notification payload received: $body")
        }

        // 2️⃣ Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["type"]
            // TODO: sample app logic (taskId)
            Log.d(TAG, "Data payload received: $type")
        }

        // 3️⃣ Show notification if we have title and body
        if (title != null && body != null) {
            showNotification(title, body)
        }
    }

    // ======================================================================================== //
    //    Displays a local notification
    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val boldTitle = SpannableString(title ?: "")
        boldTitle.setSpan(StyleSpan(Typeface.BOLD), 0, boldTitle.length, 0)
        val builder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(boldTitle)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    } // ======================================================================================== //
    /**
     * Called when Firebase generates a NEW FCM token.
     *
     * WHY:
     * - Token can change anytime.
     * - When it changes, topic subscriptions are lost.
     *
     * WHAT WE DO:
     * 1. Send new token to backend
     * 2. Restore topic subscriptions
     */
    override fun onNewToken(token : String) {
        super.onNewToken(token);
        Log.d("FCM", "New token: $token");

        sendTokenToBackend(token);

        // Re-subscribe to topics
        val sessionManager = SessionManager(applicationContext)
        FcmTopicManager.restoreSubscriptions(sessionManager)
    }
    // ======================================================================================== //
    /**
     * Sends the FCM token to backend server.
     *
     * WHY:
     * - Backend needs token for direct notifications.
     *
     * USED:
     * - On token refresh
     * - On login
     */
    private fun sendTokenToBackend(token: String) {

        CoroutineScope(Dispatchers.IO).launch {

            val result = deviceRepository.registerToken(token)

            result.onSuccess {
                Log.d("FCM", "Token sent successfully")
            }.onFailure {
                Log.e("FCM", "Failed to send token: ${it.message}")
            }
        }
    }

    companion object {
        private const val TAG = "FCM"
    }
}
