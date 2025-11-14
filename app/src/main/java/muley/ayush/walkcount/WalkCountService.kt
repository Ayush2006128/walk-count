package muley.ayush.walkcount

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat

class WalkCountService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null

    // The total steps counted by the sensor since the device was last booted.
    private var totalStepsSinceBoot = 0

    // The initial step count recorded when the service starts.
    // We subtract this from totalStepsSinceBoot to get the count for this "session".
    private var initialStepCount = -1

    private val notificationId = 1
    private val notificationChannelId = "WalkCountChannel"

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCounterSensor == null) {
            // Sensor not available
            stopSelf() // Stop the service if sensor is not found
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create the notification channel
        createNotificationChannel()

        // Start the service in the foreground
        startForeground(notificationId, createNotification(0))

        // Register the sensor listener
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        // If the service is killed, restart it
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                totalStepsSinceBoot = it.values[0].toInt()

                // If this is the first event, record the initial step count
                if (initialStepCount == -1) {
                    initialStepCount = totalStepsSinceBoot
                }

                // Calculate steps for this session
                val currentSteps = totalStepsSinceBoot - initialStepCount

                // Update the repository
                WalkCountRepository.updateSteps(currentSteps)

                // Update the notification
                updateNotification(currentSteps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this app
    }

    private fun createNotification(steps: Int): Notification {
        // Intent to open the app when notification is tapped
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Walk Count")
            .setContentText("Steps: $steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true) // Don't alert for every update
            .build()
    }

    private fun updateNotification(steps: Int) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification(steps)
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            notificationChannelId,
            "Walk Count Channel",
            NotificationManager.IMPORTANCE_LOW // Low importance for less intrusion
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Unregister the sensor listener when service is destroyed
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }
}
