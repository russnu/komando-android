# 📱 Komando Mobile - Android Application

## 🛠 Prerequisites

* Android Studio (latest stable)
* Android SDK 34+
* Gradle (installed automatically by Android Studio)
* Running Komando Spring Boot Backend
* Firebase project with FCM enabled
* `google-services.json` file (shared privately by the project owner)

---

## 📦 Setup

### 1. Clone the repository

```
git clone https://github.com/russnu/komando-android.git
cd komando-android
```

Open the project in **Android Studio**.

### 2. Firebase Configuration

This project uses Firebase Cloud Messaging (FCM) for push notifications.

* The Firebase configuration file will be shared privately by the project owner.
* Place the file in the following directory:

```
app/google-services.json
```

* Ensure the package name in Firebase matches the Android project package name.

Example:

```
org.russel.komandoandroid
```

### 3. Sync Gradle Dependencies

When opening the project, Android Studio will prompt a Gradle sync.

Click:

```
Sync Project with Gradle Files
```

This will download all required dependencies.

### 4. Backend Configuration

The Android application connects to the Komando Spring Boot backend.

Update the backend base URL if necessary.

Example:

```
http://10.0.2.2:8080
```

Notes:

* `10.0.2.2` is used when running the backend locally and the app in the Android emulator.
* If using a physical device, replace it with your computer's local IP address.

Example:

```
http://192.168.1.10:8080
```

Ensure the backend is running before launching the mobile app.


### 5. Run the Application

Connect an Android device or start an emulator.

Then run the project from Android Studio.

```
Run → Run 'app'
```

The app should build and launch automatically.

---

## 📂 Project Structure

```
komandoandroid/                                      --> parent project (Android Studio)
└── app/src/main/
    ├── java/org.russel.komandoandroid/             
    │   ├── data/                                    --> data layer module
    │   │   ├── auth/
    │   │   │   ├── AuthInterceptor.kt               --> intercepts HTTP requests to attach authentication tokens
    │   │   │   └── SessionManager.kt                --> Manages local user session and stored login data
    │   │   ├── model                                --> DTOs and request/response models
    │   │   ├── remote/                              --> remote API service interfaces using Retrofit
    │   │   │   ├── AuthApi.kt                       --> API endpoints for user authentication
    │   │   │   ├── DeviceApi.kt                     --> API endpoints for device registration (FCM token handling)
    │   │   │   ├── GroupApi.kt                      --> API endpoints for group-related operations
    │   │   │   ├── TaskApi.kt                       --> API endpoints for task-related operations
    │   │   │   └── RetrofitClient.kt                --> Configures and provides Retrofit HTTP client instance
    │   │   └── repository/                          --> repository classes that manage data operations API calls (calls AuthApi, etc.)
    │   │       ├── AuthRepository.kt
    │   │       ├── DeviceRepository.kt
    │   │       ├── GroupRepository.kt
    │   │       └── TaskRepository.kt
    │   ├── fcmservice/                              --> integration with Firebase Cloud Messaging
    │   │   ├── FcmTopicManager.kt                   --> manages topic subscriptions for push notifications
    │   │   └── KomandoFirebaseMessagingService.kt   --> Handles incoming push notifications from Firebase
    │   ├── ui/                                      --> presentation layer built using Jetpack Compose
    │   │   ├── component                            --> reusable UI components
    │   │   ├── factory/
    │   │   │   └── ViewModelFactories.kt            --> provides ViewModel factory implementations
    │   │   ├── group                                --> group screen, viewmodel, and related UI components
    │   │   ├── login                                --> login screen, viewmodel, and related UI components
    │   │   ├── profile                              --> user profile screen, viewmodel, and related UI components
    │   │   ├── task                                 --> task screen, viewmodel, and related UI components
    │   │   └── theme                                --> application theme, colors, and typography configuration
    │   └── MainActivity.kt                          --> main entry point of the Android application
    ├── res/                                         --> Application resources (layouts, images, styles, etc.)
    │   └── drawable                                 --> image and drawable resource files (icons, SVGs)
    └── AndroidManifest.xml                          --> application configuration file declaring components and permissions
```


---

## 🔒 Security Notes

The following files are excluded from the repository:

```
google-services.json
local.properties
```

These contain environment-specific configuration and should not be committed.

---

## ⚙️ Backend Dependency

This mobile application requires the Komando Spring Boot backend to be running.

Backend repository:

```
https://github.com/russnu/komando-backend
```

Ensure the backend, database, and Kafka services are running before starting the mobile app.