# 🌤️ SkyCast - Kotlin Weather App

SkyCast is a modern, location-based weather forecasting Android application built using **Kotlin**, **Jetpack Compose**, and a modular, scalable architecture. It provides real-time and periodic weather updates using the [Open-Meteo API](https://open-meteo.com/), with support for **Google OAuth**, **offline caching**, and **favorite locations** via **Supabase**.

---

## 🚀 Features

- 📍 **Location-based Weather** — Requests user's current location to fetch accurate weather data.
- 🔄 **Background Weather Sync** — Uses `WorkManager` for periodic background weather updates.
- ☁️ **Open-Meteo API Integration** — Fetches real-time weather forecasts from Open-Meteo.
- 🧭 **Jetpack Compose UI** — Smooth and reactive UI using Google's latest Compose toolkit.
- 🧱 **Modular Architecture** — Clean, scalable structure with separated modules for data, domain, UI, and core utilities.
- 💾 **Offline Caching** — Caches current location data and weather forecasts for fast loading.
- 🌐 **Google OAuth via Supabase** — Secure authentication and cloud storage for user data.
- ⭐ **Favorite Locations** — Save favorite places to track their weather conveniently.
- 🔐 **Type-safe Navigation** — Fully type-safe screen navigation using `Compose Navigation`.
- 🧪 **Kotlin Coroutines + Flow** — For reactive state management and async tasks.
- 🛠️ **Dependency Injection with Hilt** — For managing app-wide dependencies.
- 🔗 **Retrofit + Sandwich** — Modern network layer using Retrofit and [Skydoves/Sandwich](https://github.com/skydoves/Sandwich) for response handling and error states.

---

# 🌤️ SkyCast - Kotlin Weather App

SkyCast is a modern, location-based weather forecasting Android application built using **Kotlin**, **Jetpack Compose**, and a modular, scalable architecture. It provides real-time and periodic weather updates using the [Open-Meteo API](https://open-meteo.com/), with support for **Google OAuth**, **offline caching**, and **favorite locations** via **Supabase**.

---

## 🚀 Features

- 📍 **Location-based Weather** — Requests user's current location to fetch accurate weather data.
- 🔄 **Background Weather Sync** — Uses `WorkManager` for periodic background weather updates.
- ☁️ **Open-Meteo API Integration** — Fetches real-time weather forecasts from Open-Meteo.
- 🧭 **Jetpack Compose UI** — Smooth and reactive UI using Google's latest Compose toolkit.
- 🧱 **Modular Architecture** — Clean, scalable structure with separated modules for app, presentation and core(data, sync, supabase, models, network, design, database) utilities.
- 💾 **Offline Caching** — Caches current location data and weather forecasts for fast loading.
- 🌐 **Google OAuth via Supabase** — Secure authentication and cloud storage for user data.
- ⭐ **Favorite Locations** — Save favorite places to track their weather conveniently.
- 🔐 **Type-safe Navigation** — Fully type-safe screen navigation using `Compose Navigation`.
- 🧪 **Kotlin Coroutines + Flow** — For reactive state management and async tasks.
- 🛠️ **Dependency Injection with Hilt** — For managing app-wide dependencies.
- 🔗 **Retrofit + Sandwich** — Modern network layer using Retrofit and [Skydoves/Sandwich](https://github.com/skydoves/Sandwich) for response handling and error states.

---

## 📱 Screenshots



---

## 🧠 Tech Stack

| Layer         | Library / Tool                            |
|---------------|-------------------------------------------|
| Language      | Kotlin                                    |
| UI            | Jetpack Compose                           |
| Auth & Cloud  | Supabase (Google OAuth + Remote Storage)  |
| Architecture  | Modularized Clean Architecture (Domain/Data/UI/Core) |
| DI            | Hilt                                      |
| Network       | Retrofit + Skydoves Sandwich              |
| Async         | Coroutines, Kotlin Flow                   |
| Background    | WorkManager (Periodic Request)            |
| Navigation    | Jetpack Compose Navigation (Type-safe)    |
| Caching       | Local Caching (Location + Weather State)  |
| Permissions   | Runtime Location Permissions              |
| Build Tools   | Gradle (KTS), Jetpack Libraries           |

---

## 📦 Modules Overview



---

## 🧠 Tech Stack

| Layer         | Library / Tool                            |
|---------------|-------------------------------------------|
| Language      | Kotlin                                    |
| UI            | Jetpack Compose                           |
| Auth & Cloud  | Supabase (Google OAuth + Remote Storage)  |
| Architecture  | Modularized Clean Architecture (Domain/Data/UI/Core) |
| DI            | Hilt                                      |
| Network       | Retrofit + Skydoves Sandwich              |
| Async         | Coroutines, Kotlin Flow                   |
| Background    | WorkManager (Periodic Request)            |
| Navigation    | Jetpack Compose Navigation (Type-safe)    |
| Caching       | Local Caching (Location + Weather State)  |
| Permissions   | Runtime Location Permissions              |
| Build Tools   | Gradle (KTS), Jetpack Libraries           |

---




---

## 🔐 Permissions

SkyCast requests the following permissions:

- `ACCESS_FINE_LOCATION`: To fetch accurate weather based on user's real-time location
- `ACCESS_COARSE_LOCATION`: For fallback if precise location is unavailable
- Internet access for fetching weather and syncing with Supabase

---

## 🧪 Architecture Overview

SkyCast follows a **modular clean architecture** inspired by industry standards:

- **UI Layer (Features)** — Jetpack Compose UIs observing state from ViewModels.
- **Domain Layer** — Business logic via use cases; interfaces for abstraction.
- **Data Layer** — Retrofit for remote, Supabase SDK, Room (optional) for caching.
- **Core Layer** — Contains shared utilities, design system, constants, base classes.

---

## 🔁 Background Sync with WorkManager

SkyCast uses `WorkManager` to:
- Periodically fetch updated weather data for the current location.
- Store it in local cache for use in offline or slow-network conditions.
- Ensure background syncing respects battery optimization policies.

---

## 🧩 Network Stack

- API: [https://open-meteo.com](https://open-meteo.com/)
- Retrofit client handles base requests
- Responses wrapped in **ApiResponse** using `sandwich` for:
  - Success
  - Empty
  - Error (mapped to UI error states)

---

## ☁️ Supabase Integration

Supabase is used for:
- Google Sign-In (OAuth)
- Remote storage of favorite locations (per user)
- User session persistence(for proper Locations Screen Rendering)

---

## 💡 Getting Started

### 🔨 Prerequisites
- Android Studio Meerkat or latest version
- Kotlin 2.0.21
- Supabase project credentials (for OAuth and database)
- Open-Meteo API (no key needed, but follow terms of use)

### ⚙️ Setup

```bash
git clone https://github.com/joelmuraguri/skycast.git
cd skycast


## 📦 Modules Overview

