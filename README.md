# Patient Health Monitoring System

A complete Android Studio project for a hospital-style Patient Health Monitoring System built with Kotlin, Jetpack Compose, Material Design 3, Room Database, MVVM, repository pattern, and Navigation Compose.

## Included Features

- Secure staff registration and login
- Salted SHA-256 password hashing before local storage
- Persistent login session until logout
- Room/SQLite staff and patient tables
- Auto-generated unique patient IDs in the `PAT-000001` format
- Add, view, edit, and delete patients
- Delete confirmation dialog
- Search by patient name or patient ID
- Sorting by patient name, recently added, oldest, and age
- Comprehensive patient medical profile form
- Clear validation messages for required and invalid fields
- Clean medical blue/white/green Material 3 UI

## Build

The project was verified locally with:

```powershell
.\gradlew.bat assembleDebug
```

The generated debug APK is:

```text
app/build/outputs/apk/debug/app-debug.apk
```

This workspace also includes a copied APK at:

```text
../PatientHealthMonitoringSystem-debug.apk
```

## Architecture

- `data/local`: Room database, DAOs, and entities
- `data/repository`: Auth and patient repositories
- `session`: SharedPreferences-backed session manager
- `viewmodel`: MVVM state and validation logic
- `ui/screens`: Compose screens for auth, home, patient form, and details
- `navigation`: Navigation graph and routes

The structure is ready for future modules such as IoT sensor integrations, cloud sync, notifications, ECG streams, ESP32/Arduino connectivity, and doctor dashboards.
