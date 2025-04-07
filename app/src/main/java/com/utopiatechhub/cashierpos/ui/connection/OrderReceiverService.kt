package com.utopiatechhub.cashierpos.ui.connection

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.utopiatechhub.cashierpos.R
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel

class OrderReceiverService : android.app.Service() {
    private var server: OrderHttpServer? = null
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var otherOrderViewModel: OtherOrderViewModel

    override fun onCreate() {
        super.onCreate()
        orderViewModel = OrderViewModel(OrderRepository(AppDatabase.getDatabase(this).orderDao()))
        otherOrderViewModel = OtherOrderViewModel(OtherOrderRepository(AppDatabase.getDatabase(this).otherOrderDao()))

        // Start the HTTP server
        server = OrderHttpServer(orderViewModel, otherOrderViewModel) { message ->
            Log.d("OrderReceiverService", "Received Order: $message")
        }
        server?.start()

        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "ORDER_CHANNEL"
        val channelName = "Order Receiver"

        // Ensure the notification channel exists (required for API 26+)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW // Use LOW importance for background services
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)

        // Build a valid notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Receiver Running")
            .setContentText("Listening for orders...")
            .setSmallIcon(R.drawable.baseline_notifications_active_24) // Ensure this icon exists in res/drawable
            .setPriority(NotificationCompat.PRIORITY_LOW) // Use LOW priority to avoid issues
            .build()

        // Start the foreground service with the valid notification
        startForeground(1, notification)
    }

    override fun onDestroy() {
        server?.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
