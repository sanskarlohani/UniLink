# UniLink

UniLink is an Android app for university communities to report and browse lost-and-found items. It’s built with Jetpack Compose and Firebase, following a simple MVVM architecture.

## Features
- Email/password authentication (Firebase Auth)
- Create and edit user profiles (Firestore)
- Report lost or found items (title, description, location, status)
- Browse Lost and Found lists, view item details
- Modern UI with Compose, Material 3, and Navigation

## Tech Stack
- Kotlin, Coroutines, StateFlow
- Jetpack Compose (Material 3, Navigation)
- AndroidX ViewModel & Lifecycle
- Firebase: Authentication, Firestore, Realtime Database
- Gson (local user cache), Coil (image loading)

## Requirements
- Android Studio (Hedgehog/Koala or newer)
- Android SDK 35
- JDK 11
- A Firebase project with Android app id `com.sanskar.unilink`


## Build & Run
Using Android Studio:
- Open the project root and let Gradle sync
- Select a device/emulator
- Run the app

Using Gradle (Windows/macOS/Linux):
```bash
./gradlew assembleDebug
./gradlew installDebug
```

## Project Structure
```
app/
  src/main/
    AndroidManifest.xml
    java/com/sanskar/unilink/
      MainActivity.kt
      AppNav.kt
      Resource.kt
      Routes.kt
      SharedPreferenceManager.kt
      UniLinkApp.kt
      models/
        LostFoundItem.kt
        User.kt
      repository/
        Repository.kt
      screens/
        LoginScreen.kt
        SignUpScreen.kt
        HomeScreen.kt
        LostScreen.kt
        FoundScreen.kt
        ItemDetailScreen.kt
        ReportItemScreen.kt
        ProfileScreen.kt
      ui/theme/
        Color.kt, Theme.kt, Type.kt
```

## Architecture
- MVVM with a Repository layer
- State management via `StateFlow` and a `Resource<T>` wrapper
- Repository uses Firebase Auth, Firestore (`User`, `LostItems`, `FoundItems`) and Realtime DB (`LostLiveItems`, `FoundLiveItems`)

## Data Models
- `User`: name, email, sic, year, semester, college, uid
- `LostFoundItem`: id, title, description, location, type (lost|found), userId, timestamp, status (active|claimed)

## Contributing
- Keep UI in Compose; business logic in ViewModel/Repository
- Use coroutines and `Resource` for async results
- Add new routes in `Routes.kt` and wire them in `AppNav.kt`

## License

