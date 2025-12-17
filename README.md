# WalkCount

WalkCount is a simple, privacy-focused Android application designed to track your daily steps and help you achieve your fitness goals. Built with modern Android development standards, it leverages the device's built-in sensors to count steps efficiently without draining your battery.

## Features

*   **Real-time Step Counting**: accurate tracking using the device's hardware step sensor.
*   **Background Tracking**: Continue counting steps even when the app is closed, thanks to a persistent foreground service.
*   **Goal Setting**: Set your own daily step goals and track your progress.
*   **Privacy First**: All data is processed and stored locally on your device. No internet connection is required, and no data is ever uploaded to the cloud.
*   **Modern UI**: Clean and responsive interface built with Jetpack Compose and Material 3 design.

## Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Data Persistence**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preferences)
*   **Background Processing**: Android Foreground Service & [SensorManager](https://developer.android.com/reference/android/hardware/SensorManager) API

## Prerequisites

*   Android SDK 29 (Android 10) or higher.
*   A device with a built-in step counter sensor.

## Permissions

The app requires the following permissions to function correctly:

*   `ACTIVITY_RECOGNITION`: To access the step counter sensor data.
*   `FOREGROUND_SERVICE` & `FOREGROUND_SERVICE_HEALTH`: To run the step counting service in the background.
*   `POST_NOTIFICATIONS`: To display the current step count in the notification shade (Android 13+).

## Installation

1.  Clone the repository:
    ```bash
    git clone https://github.com/Ayush2006128/walk-count.git
    ```
2.  Open the project in [Android Studio](https://developer.android.com/studio).
3.  Sync the project with Gradle files.
4.  Run the app on your physical device (Emulators may not support the hardware step sensor).

## License

This project is open-source and available under the MIT License.
