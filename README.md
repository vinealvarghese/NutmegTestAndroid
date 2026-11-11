# NutmegTest (Android)

Kotlin / Jetpack Compose port of the iOS `NutmegTest` app. Same architecture and
API calls (jsonplaceholder.typicode.com), just Android-native:

- **API** (`api/`) — `Networker` interface + `DefaultNetworker` (OkHttp + Gson),
  mirrors `Networking.swift` / `DefaultNetworker.swift`.
- **Models** (`model/`) — `Post`, `User`, `Comment`, `PostModel` (post + its author).
- **Use cases** (`usecase/`) — `GetPostsUseCase` zips posts + users, same as the
  Combine `Zip` in the iOS version.
- **ViewModels** (`ui/posts`, `ui/comments`) — `androidx.lifecycle.ViewModel` +
  `StateFlow` in place of `ObservableObject` + `@Published`/Combine.
- **Compose screens** — `PostsScreen` and `CommentsScreen` replace `PostsView`
  and `CommentsView`; `PostListItemView` is a shared composable, same as the
  shared SwiftUI view.
- **Navigation** — Navigation Compose (`navigation/NutmegNavHost.kt`) replaces
  the SwiftUI `NavigationStack`.

Unit tests under `app/src/test` mirror `PostsViewModelTests.swift` /
`MockNetworker.swift`.

## Opening the project

Open this folder in Android Studio (Hedgehog or newer) and let it sync — it
will generate the Gradle wrapper jar automatically if it's missing. If you
want to build from the command line instead, run `gradle wrapper` once from
this directory (any locally installed Gradle 8.x works) to populate
`gradle/wrapper/gradle-wrapper.jar`, then use `./gradlew` as usual.

- `./gradlew assembleDebug` — build the debug APK
- `./gradlew test` — run unit tests
- `./gradlew installDebug` — install on a connected device/emulator

Minimum SDK 24, target/compile SDK 34, Kotlin 1.9.24, Compose BOM 2024.06.00.
