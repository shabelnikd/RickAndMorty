# RickAndMorty Android Project - Gemini Context

## Project Overview
This is a modern Android application built with **Kotlin** and **Jetpack Compose**. It serves as a client for the **Rick and Morty API**, allowing users to browse characters, locations, and episodes. The project implements **Clean Architecture** principles and utilizes a modern Android technology stack including **Koin**, **Ktor**, **Paging 3**, and **Room**.

## Tech Stack & Libraries

*   **Language**: Kotlin 2.1.20 (Target JVM 17)
*   **UI Framework**: Jetpack Compose (Material 3)
*   **Build System**: Gradle (Kotlin DSL) with Version Catalog (`libs.versions.toml`)
*   **Architecture Pattern**: MVVM (Model-View-ViewModel) + Clean Architecture
*   **Dependency Injection**: Koin 4.0.2
*   **Networking**: Ktor Client 3.1.2 (OkHttp engine, ContentNegotiation, Kotlinx Serialization)
*   **Local Database**: Room 2.7.1 (using KSP)
*   **Concurrency**: Kotlin Coroutines & Flow
*   **Pagination**: Paging 3 (PagingSource + PagingData + Compose integration)
*   **Image Loading**: Coil 3
*   **UI Effects**: 
    *   **Haze**: Glassmorphism/blur effects (`dev.chrisbanes.haze`)
    *   **Lottie**: Vector animations
    *   **Compose Shimmer**: Loading skeleton effects

## Architecture Structure

The application follows a strict Clean Architecture separation within the `app` module:

1.  **Domain Layer** (`domain`): Pure business logic, models, and repository interfaces. No Android dependencies.
2.  **Data Layer** (`data`): Implementation of domain repositories, API networking, and local database storage.
3.  **UI Layer** (`ui`): Presentation logic, ViewModels, and Jetpack Compose screens.

## Package Hierarchy

Root: `com.shabelnikd.rickandmorty`

```text
com.shabelnikd.rickandmorty
├── App.kt                       # Application entry point (Koin init)
├── data/                        # DATA LAYER
│   ├── core/
│   │   ├── di/                  # Koin modules (dataModule, ktorModule)
│   │   ├── room/                # Database setup (AppDatabase)
│   │   └── utils/               # Network helpers (ApiErrorResponse, makeRequest)
│   ├── datasource/
│   │   ├── local/               # DAOs (FavoriteCharacterDao)
│   │   └── network/             # API service definitions
│   ├── mappers/                 # DTO -> Domain mappers
│   ├── models/                  # DTOs
│   │   ├── characters/          # CharacterDto, etc.
│   │   ├── episodes/            # EpisodeDto, etc.
│   │   └── locations/           # LocationDto, etc.
│   └── repository/              # Repository Implementations
├── domain/                      # DOMAIN LAYER
│   ├── models/                  # Pure Domain Models
│   │   ├── characters/          # CharacterModel, etc.
│   │   ├── episodes/            # Episode, etc.
│   │   └── locations/           # Location, etc.
│   ├── repository/              # Repository Interfaces
│   └── usecases/                # Business Logic Units
└── ui/                          # UI LAYER
    ├── activity/                # MainActivity
    ├── base/                    # Shared ViewModel logic (BaseViewModel)
    ├── components/              # Shared UI Widgets
    │   ├── RefreshableScaffoldPagingList.kt  # Core List Component
    │   ├── CharacterListItem.kt
    │   ├── LoadingIndicator.kt
    │   └── ...
    ├── navigation/              # NavHost, NavigationStack, Screens
    ├── screens/                 # Feature Screens
    │   ├── characters/
    │   ├── episodes/
    │   └── locations/
    └── theme/                   # Material 3 Theme (Color, Type)
```

## Key Files & Entry Points

*   **Application Class**: `app/src/main/java/com/shabelnikd/rickandmorty/App.kt`
    *   Initializes Koin with `uiModule`, `dataModule`, and `ktorModule`.
*   **Build Configuration**: `app/build.gradle.kts` & `gradle/libs.versions.toml`
*   **Common List Component**: `app/src/main/java/com/shabelnikd/rickandmorty/ui/core/base/components/RefreshableScaffoldPagingList.kt`
    *   A generic scaffolding component handling Paging 3 lists, pull-to-refresh, and Haze effects.

## Building and Running

The project uses the Gradle Wrapper.

**Build Debug APK:**
```bash
./gradlew assembleDebug
```

**Run Unit Tests:**
```bash
./gradlew testDebugUnitTest
```

**Run Instrumentation Tests:**
```bash
./gradlew connectedDebugAndroidTest
```

## Development Conventions

*   **DI**: Use Koin `module { ... }`. Inject ViewModels using `koinViewModel()`.
*   **Networking**: Use `Ktor` HttpClient. Handle errors via `runCatching` or specific wrappers in the Data layer.
*   **Paging**: Use `Pager` in repositories to return `Flow<PagingData<T>>`. Collect in UI using `collectAsLazyPagingItems()`.
*   **Styling**: Use Material 3 tokens. Apply Haze effects to `Scaffold` or backgrounds for visual depth.
*   **Localization**: All user-facing strings should be in `strings.xml`.
