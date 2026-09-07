package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AudioPlaybackService : Service() {

    companion object {
        const val CHANNEL_ID = "resonance_frequency_playback"
        const val NOTIFICATION_ID = 1001

        const val ACTION_PLAY = "com.example.resonance.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.resonance.ACTION_PAUSE"
        const val ACTION_TOGGLE = "com.example.resonance.ACTION_TOGGLE"
        const val ACTION_STOP = "com.example.resonance.ACTION_STOP"
        const val ACTION_NEXT = "com.example.resonance.ACTION_NEXT"
        const val ACTION_PREV = "com.example.resonance.ACTION_PREV"

        fun start(context: Context) {
            val intent = Intent(context, AudioPlaybackService::class.java).apply {
                action = ACTION_PLAY
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun pause(context: Context) {
            val intent = Intent(context, AudioPlaybackService::class.java).apply {
                action = ACTION_PAUSE
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, AudioPlaybackService::class.java).apply {
                action = ACTION_STOP
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        // Observe playback state to update notification dynamically
        serviceScope.launch {
            PlaybackStateManager.currentPreset.collectLatest {
                updateNotification()
            }
        }
        serviceScope.launch {
            PlaybackStateManager.isPlaying.collectLatest {
                updateNotification()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                startInForeground()
                if (!PlaybackStateManager.isPlaying.value) {
                    PlaybackStateManager.audioEngine.play()
                }
                updateNotification()
            }
            ACTION_PAUSE -> {
                PlaybackStateManager.pause()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                } else {
                    @Suppress("DEPRECATION")
                    stopForeground(false)
                }
                isForeground = false
                updateNotification()
            }
            ACTION_TOGGLE -> {
                if (PlaybackStateManager.isPlaying.value) {
                    PlaybackStateManager.pause()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_DETACH)
                    } else {
                        @Suppress("DEPRECATION")
                        stopForeground(false)
                    }
                    isForeground = false
                    updateNotification()
                } else {
                    startInForeground()
                    PlaybackStateManager.audioEngine.play()
                    updateNotification()
                }
            }
            ACTION_STOP -> {
                PlaybackStateManager.pause()
                stopForeground(STOP_FOREGROUND_REMOVE)
                isForeground = false
                stopSelf()
            }
            ACTION_NEXT -> {
                PlaybackStateManager.playNext(this)
            }
            ACTION_PREV -> {
                PlaybackStateManager.playPrevious(this)
            }
            else -> {
                if (PlaybackStateManager.isPlaying.value) {
                    startInForeground()
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startInForeground() {
        val notification = buildNotification()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val serviceType = ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                startForeground(NOTIFICATION_ID, notification, serviceType)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
            isForeground = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        if (PlaybackStateManager.currentPreset.value == null) {
            manager.cancel(NOTIFICATION_ID)
            return
        }
        if (isForeground) {
            manager.notify(NOTIFICATION_ID, buildNotification())
        } else if (PlaybackStateManager.isPlaying.value) {
            startInForeground()
        } else {
            manager.notify(NOTIFICATION_ID, buildNotification())
        }
    }

    private fun buildNotification(): Notification {
        val preset = PlaybackStateManager.currentPreset.value
        val isPlaying = PlaybackStateManager.isPlaying.value

        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val toggleIntent = Intent(this, AudioPlaybackService::class.java).apply {
            action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
        }
        val togglePendingIntent = PendingIntent.getService(
            this, 1, toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextIntent = Intent(this, AudioPlaybackService::class.java).apply {
            action = ACTION_NEXT
        }
        val nextPendingIntent = PendingIntent.getService(
            this, 2, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val prevIntent = Intent(this, AudioPlaybackService::class.java).apply {
            action = ACTION_PREV
        }
        val prevPendingIntent = PendingIntent.getService(
            this, 3, prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, AudioPlaybackService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 4, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = preset?.title ?: "Harmonic Frequency Player"
        val subtitle = preset?.let { "${it.category} • ${it.technical_hz}" } ?: "Real-time synthesized audio"

        val playPauseIcon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        val playPauseTitle = if (isPlaying) "Pause" else "Play"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setContentIntent(openAppPendingIntent)
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(android.R.drawable.ic_media_previous, "Previous", prevPendingIntent)
            .addAction(playPauseIcon, playPauseTitle, togglePendingIntent)
            .addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Resonance Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls and status for real-time wellness frequency audio"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
