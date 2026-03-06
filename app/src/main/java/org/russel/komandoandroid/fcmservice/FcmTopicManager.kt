package org.russel.komandoandroid.fcmservice

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import org.russel.komandoandroid.data.auth.SessionManager

/**
 * Singleton responsible for managing all Firebase Cloud Messaging (FCM) topic subscriptions in the app.
 */
object FcmTopicManager {

    // ===================================================================================== //

    /**
     * Subscribes the device to a specific group topic (group-{groupId}).
     * USED:
     * - After successful login
     * - After fetching user groups
     * - Internally by subscribeToGroups()
     */
    fun subscribeToGroup(groupId: String, onComplete: ((Boolean) -> Unit)? = null) {
        FirebaseMessaging.getInstance()
            .subscribeToTopic("group-$groupId")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to group-$groupId")
                } else {
                    Log.e("FCM", "Failed to subscribe to group-$groupId", task.exception)
                }
                onComplete?.invoke(task.isSuccessful)
            }
    }

    // ===================================================================================== //

    /**
     * Unsubscribes the device from a specific group topic.
     * USED:
     * - On group removal
     * - When user leaves a group
     * - On logout (optional but recommended)
     */
    fun unsubscribeFromGroup(groupId: String, onComplete: ((Boolean) -> Unit)? = null) {
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic("group-$groupId")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Unsubscribed from group-$groupId")
                } else {
                    Log.e("FCM", "Failed to unsubscribe from group-$groupId", task.exception)
                }
                onComplete?.invoke(task.isSuccessful)
            }
    }

    // ===================================================================================== //
    /**
     * Subscribes to multiple groups at once.
     *
     * USED:
     * - After login
     * - After restoring session
     */
    fun subscribeToGroups(groupIds: List<Int>) {
        groupIds.forEach { groupId ->
            subscribeToGroup(groupId.toString())
        }
    }

    // ===================================================================================== //
    /**
     * Restores all topic subscriptions from saved session data.
     *
     * WHY:
     * - FCM token may change.
     * - App may restart.
     * - Subscriptions may be lost.
     *
     * USED:
     * - On app start (if user already logged in)
     * - Inside FirebaseMessagingService.onNewToken()
     */
    fun restoreSubscriptions(sessionManager: SessionManager) {
        if (!sessionManager.isLoggedIn()) return

        val groupIds = sessionManager.getUserGroups()
        subscribeToGroups(groupIds)
    }

}